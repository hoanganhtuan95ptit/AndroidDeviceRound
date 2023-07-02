package com.tuanhoang.deviceround

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.one.core.utils.extentions.toObjectOrNull
import com.one.task.executeAsyncAll
import com.tuanhoang.deviceround.data.task.ApiSyncTask
import com.tuanhoang.deviceround.data.task.DefaultSyncTask
import com.tuanhoang.deviceround.data.task.SyncParam
import com.tuanhoang.deviceround.entities.DeviceInfo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import kotlin.coroutines.CoroutineContext

object DeviceRound {

    const val KEY = "DEVICE_ROUND_INFO"

    private val handler = CoroutineExceptionHandler { _: CoroutineContext, _: Throwable ->
    }

    private var sharedPreferences: SharedPreferences? = null


    fun init(context: Context, retrofit: Retrofit, _sharedPreferences: SharedPreferences) = with(ProcessLifecycleOwner.get()) {

        sharedPreferences = _sharedPreferences

        lifecycleScope.launch(handler + Dispatchers.IO) {

            listOf(DefaultSyncTask(context, _sharedPreferences), ApiSyncTask(retrofit, _sharedPreferences)).executeAsyncAll(SyncParam(android.os.Build.MODEL)).collect()
        }
    }

    suspend fun fetchRound(): List<Int>? {

        return fetchRoundAsync().first()
    }

    fun fetchRoundAsync() = channelFlow {

        val sharedPreferences = sharedPreferences ?: error("need call DeviceRound.init before")

        val listener = OnSharedPreferenceChangeListener { prefs, key ->

            if (key == KEY && isActive) prefs.getString(KEY, "")?.toObjectOrNull(DeviceInfo::class.java).let {
                trySend(it?.radius)
            }
        }

        listener.onSharedPreferenceChanged(sharedPreferences, KEY)

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {

            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        }
    }.flowOn(handler + Dispatchers.IO)
}
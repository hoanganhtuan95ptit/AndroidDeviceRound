package com.tuanhoang.deviceround.data.task

import android.content.SharedPreferences
import com.one.core.utils.extentions.toJson
import com.one.core.utils.extentions.toObjectOrNull
import com.tuanhoang.deviceround.DeviceRound
import com.tuanhoang.deviceround.entities.DeviceInfo
import kotlinx.coroutines.delay
import retrofit2.Retrofit
import retrofit2.http.GET


class ApiSyncTask(
    private val retrofit: Retrofit,
    private val sharedPreferences: SharedPreferences,
) : SyncTask {

    override suspend fun executeTask(param: SyncParam) {

        val deviceInfoCache = sharedPreferences.getString(DeviceRound.KEY, "")?.toObjectOrNull(DeviceInfo::class.java)

        if (deviceInfoCache != null && System.currentTimeMillis() - deviceInfoCache.timeUpdate < 3 * 24 * 60 * 60 * 1000) {

            return
        }

        if (deviceInfoCache != null) {

            delay(60 * 1000)
        }

        retrofit.create(DeviceRoundService::class.java).fetchChainList().find {

            it.name.equals(param.deviceModel, true)
        }?.let {

            it.timeUpdate = System.currentTimeMillis()

            sharedPreferences.edit().putString(DeviceRound.KEY, it.toJson()).apply()
        }
    }

    private interface DeviceRoundService {

        @GET("https://raw.githubusercontent.com/hoanganhtuan95ptit/DeviceRound/main/library/src/main/res/raw/data.json")
        suspend fun fetchChainList(): List<DeviceInfo>
    }
}
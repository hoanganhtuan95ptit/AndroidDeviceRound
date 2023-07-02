package com.tuanhoang.deviceround.data.task

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.one.core.utils.extentions.toJson
import com.one.core.utils.extentions.toListObject
import com.one.task.LowException
import com.tuanhoang.deviceround.BuildConfig
import com.tuanhoang.deviceround.DeviceRound
import com.tuanhoang.deviceround.R
import com.tuanhoang.deviceround.entities.DeviceInfo
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class DefaultSyncTask(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
) : SyncTask {

    override fun priority(): Int = Int.MAX_VALUE

    override suspend fun executeTask(param: SyncParam) {

        if (sharedPreferences.getString(DeviceRound.KEY, "")?.isNotBlank() == true) {

            throw LowException("break")
        }

        if (BuildConfig.DEBUG) {

            Log.d("tuanha", "executeTask: deviceName:${param.deviceModel}")
        }

        readTextFile(context.resources.openRawResource(R.raw.data)).toListObject(DeviceInfo::class.java).find {

            it.name.equals(param.deviceModel, true)
        }?.let {

            sharedPreferences.edit().putString(DeviceRound.KEY, it.toJson()).apply()
        } ?: let {

            throw LowException("not found device")
        }
    }

    private fun readTextFile(inputStream: InputStream): String {

        val outputStream = ByteArrayOutputStream()

        val buf = ByteArray(1024)

        var len: Int

        try {
            while (inputStream.read(buf).also { len = it } != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream.close()
        } catch (_: IOException) {
        }

        return outputStream.toString()
    }
}
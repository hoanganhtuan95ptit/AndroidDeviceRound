package com.tuanhoang.deviceround

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.RoundedCorner
import androidx.annotation.RequiresApi

object DeviceRound {

    private val deviceNameAndRadius = hashMapOf(
        "emulator64_arm64" to intArrayOf(98, 98, 98, 98),
        "Pixel 3" to intArrayOf(44, 44, 44, 44),
        "Pixel 5" to intArrayOf(100, 100, 100, 100),
    )

    fun getRoundByDeviceName(activity: Activity, deviceName: String, round: (IntArray) -> Unit) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

        getRound(deviceName, round)
    } else {

        getRound(deviceName, round)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun getRoundS(activity: Activity, round: (IntArray) -> Unit) {

        activity.window.decorView.post {

            Log.d("tuanha", "getRoundS: ")

            val topLeft = activity.window.decorView.rootWindowInsets.getRoundedCorner(RoundedCorner.POSITION_TOP_LEFT)
            val topRight = activity.window.decorView.rootWindowInsets.getRoundedCorner(RoundedCorner.POSITION_TOP_RIGHT)
            val bottomLeft = activity.window.decorView.rootWindowInsets.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_RIGHT)
            val bottomRight = activity.window.decorView.rootWindowInsets.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_LEFT)

            round(intArrayOf(topLeft?.radius ?: 0, topRight?.radius ?: 0, bottomLeft?.radius ?: 0, bottomRight?.radius ?: 0))
        }
    }

    private fun getRound(deviceName: String, round: (IntArray) -> Unit) {

        val radius = deviceNameAndRadius[deviceName] ?: intArrayOf(0, 0, 0, 0)

        round(radius)
    }
}
package com.tuanhoang.deviceround.example

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.jaredrummler.android.device.DeviceName
import com.tuanhoang.deviceround.DeviceRound


class MainActivity : AppCompatActivity() {

    private lateinit var binding: com.tuanhoang.deviceround.example.databinding.ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        binding = com.tuanhoang.deviceround.example.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DeviceName.with(this).request { info, error ->

            DeviceRound.getRoundByDeviceName(this, info.name) {

                val view = findViewById<RoundedImageView>(R.id.rounded)

                view.setRadius(RoundedImageView.Corner.TOP_LEFT, it[0])
                view.setRadius(RoundedImageView.Corner.TOP_RIGHT, it[1])
                view.setRadius(RoundedImageView.Corner.BOTTOM_LEFT, it[2])
                view.setRadius(RoundedImageView.Corner.BOTTOM_RIGHT, it[3])
                view.postInvalidate()

                Log.d(
                    "tuanha", "onCreate: " +
                        "\nmanufacturer:${info.manufacturer} " +
                        "\nmarketName:${info.marketName} " +
                        "\nmodel:${info.model} " +
                        "\ncodename:${info.codename} " +
                        "\nname:${info.name} " +
                        "\nradius:${it.map { it.toString() }}"
                )
            }
        }
    }
}
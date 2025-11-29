package com.example.pbo.zaky

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R
import com.example.pbo.utils.AnimationHelper
import com.example.pbo.utils.NavigationHelper

class Start : AppCompatActivity() {

    private var animHelper = AnimationHelper()
    private var navHelper = NavigationHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val energyText: View = findViewById(R.id.energyText)
        val subText: View = findViewById(R.id.subText)

        // Animasi fade in dua teks
        animHelper.fadeInTwoViews(energyText, subText) {
            // Setelah animasi selesai, tunggu sebentar lalu otomatis pindah ke Login
            Handler(Looper.getMainLooper()).postDelayed({
                navHelper.goTo(this, LogIn::class.java)
            }, 500) // 500ms delay, bisa diubah sesuai kebutuhan
        }
    }
}

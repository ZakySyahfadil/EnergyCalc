package com.example.pbo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.utils.AnimationHelper
import com.example.pbo.utils.NavigationHelper

class MainActivity : AppCompatActivity() {

    private var animHelper = AnimationHelper()
    private var navHelper = NavigationHelper()

    private var isAnimationFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val energyText: View = findViewById(R.id.energyText)
        val subText: View = findViewById(R.id.subText)

        animHelper.fadeInTwoViews(energyText, subText) {
            isAnimationFinished = true
        }

        val root: View = findViewById(android.R.id.content)
        root.setOnClickListener {
            if (isAnimationFinished) {
                navHelper.goTo(this, LogIn::class.java)
            }
        }
    }
}

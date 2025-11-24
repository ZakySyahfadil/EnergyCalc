package com.example.pbo.utils

import android.app.Activity
import android.content.Intent
import com.example.pbo.R

class NavigationHelper {
    fun goTo(activity: Activity, target: Class<*>) {
        val intent = Intent(activity, target)
        activity.startActivity(intent)
        // Tambahkan animasi fade in / fade out
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        activity.finish()
    }
}
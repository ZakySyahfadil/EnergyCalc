package com.example.pbo.utils

import android.app.Activity
import android.content.Intent

class NavigationHelper {
    fun goTo(activity: Activity, target: Class<*>) {
        val intent = Intent(activity, target)
        activity.startActivity(intent)
        activity.finish()
    }
}
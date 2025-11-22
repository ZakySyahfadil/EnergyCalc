package com.example.pbo.utils

import android.view.View

// Tes Pull Up Request
class AnimationHelper {
    fun fadeInTwoViews(first: View, second: View, onEnd: () -> Unit) {
        first.alpha = 0f
        second.alpha = 0f

        first.animate()
            .alpha(1f)
            .setDuration(1000)
            .withEndAction {
                second.animate()
                    .alpha(1f)
                    .setDuration(1300)
                    .setStartDelay(400)
                    .withEndAction { onEnd() }
                    .start()
            }
            .start()
    }
}
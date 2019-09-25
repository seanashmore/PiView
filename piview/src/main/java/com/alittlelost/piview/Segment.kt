package com.alittlelost.piview

import android.animation.ValueAnimator
import androidx.core.animation.doOnEnd

class Segment(val tag: String, val percentage: Float, val colorResId: Int) {
    var startAngle: Float = 0.0f
    var endAngle: Float = 0.0f
    var scale: Float = 1.0f

    private var scaleAnimator = ValueAnimator.ofFloat(PiView.SEGMENT_REGULAR, PiView.SEGMENT_SCALED)
    private var descaleAnimator = ValueAnimator.ofFloat(PiView.SEGMENT_SCALED, PiView.SEGMENT_REGULAR)

    fun startScaleAnimator(): ValueAnimator {
        scaleAnimator.addUpdateListener { animation -> scale = animation?.animatedValue as Float }
        scaleAnimator.doOnEnd {
            it.removeAllListeners()
            scale = PiView.SEGMENT_SCALED
        }

        descaleAnimator.cancel()
        scaleAnimator.start()

        return scaleAnimator
    }

    fun startDescaleAnimator(): ValueAnimator {
        descaleAnimator.addUpdateListener { animation -> scale = animation?.animatedValue as Float }
        descaleAnimator.doOnEnd {
            it.removeAllListeners()
            scale = PiView.SEGMENT_REGULAR
        }

        scaleAnimator.cancel()
        descaleAnimator.start()

        return descaleAnimator
    }


}
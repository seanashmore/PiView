package com.alittlelost.pietest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alittlelost.piview.PiView
import com.alittlelost.piview.Segment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val segment1 = Segment(
            "One",
            50.0f,
            R.color.colorAccent
        )
        val segment2 = Segment(
            "Two",
            32.0f,
            R.color.colorPrimary
        )
        val segment3 = Segment(
            "Three",
            18.0f,
            R.color.colorPrimaryDark
        )

        piView.addSegment(segment3)
        piView.addSegment(segment1)
        piView.addSegment(segment2)

        piView.setOnSegmentClickListener(object : PiView.OnSegmentClickListener {
            override fun onSegmentTapped(segment: Segment) {
                Log.i("SA", "Segment ${segment.tag} was tapped")
            }
        })
    }
}

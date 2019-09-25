package com.alittlelost.pietest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }
}

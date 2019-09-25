package com.alittlelost.piview

internal class SegmentManager {

    private val total = 100.0f
    var used = 0.0f
    val segments = ArrayList<Segment>()

    fun addSegment(segment: Segment): Boolean {
        return if ((total - used) >= segment.percentage) {
            segment.startAngle = 360.0f / 100 * used
            used += segment.percentage
            segment.endAngle = 360.0f / 100 * used
            segments.add(segment)
        } else {
            throw SegmentTooLargeException("Segment is too large. Segment size ${segment.percentage}, Space available ${total - used}")
        }
    }

    fun removeSegment(segment: Segment) : Boolean {
        return segments.remove(segment)
    }

    class SegmentTooLargeException(message: String) : Exception(message)
}
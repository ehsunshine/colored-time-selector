package de.ehsun.coloredtimebar

import java.text.SimpleDateFormat
import java.util.*

data class SimpleTime(val hour: Int, val minute: Int) : Comparable<SimpleTime> {

    companion object {
        @JvmStatic private val timeFormatter = SimpleDateFormat("HH:mm", Locale.GERMAN)

        @Suppress("DEPRECATION")
        @JvmStatic
        fun from(time: String): SimpleTime {
            val parsedTime = timeFormatter.parse(time)
            return SimpleTime(parsedTime.hours, parsedTime.minutes)
        }

        @JvmStatic
        fun fromMinutes(mins: Int) = SimpleTime(mins / 60, mins % 60)
    }

    override fun compareTo(other: SimpleTime) =
            (hour * 60 + minute).compareTo(other.hour * 60 + other.minute)

    fun toSeconds() = toMinutes() * 60

    fun toMinutes() = (hour * 60) + minute

    fun toTimeStamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        calendar.add(Calendar.HOUR_OF_DAY, hour)
        calendar.add(Calendar.MINUTE, minute)

        return calendar.timeInMillis / 1000L
    }

    operator fun unaryMinus() = SimpleTime(-hour, -minute)

    operator fun plus(time: SimpleTime): SimpleTime {
        var ds = toSeconds() + time.toSeconds()
        val h = ds / 3600
        ds -= h * 3600
        val m = ds / 60
        return SimpleTime(h, m)
    }

    operator fun minus(time: SimpleTime) = this + (-time)
}

operator fun ClosedRange<SimpleTime>.component1() = this.start
operator fun ClosedRange<SimpleTime>.component2() = this.endInclusive
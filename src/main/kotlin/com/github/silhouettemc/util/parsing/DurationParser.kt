package com.github.silhouettemc.util.parsing

import com.github.silhouettemc.util.plus
import java.time.Duration
import java.time.temporal.ChronoUnit

object DurationParser {

    private val durationMap = mapOf(
        listOf("ns", "nano", "nanos", "nanosecond", "nanoseconds") to ChronoUnit.NANOS,
        listOf("ms", "milli", "millis", "millisecond", "milliseconds") to ChronoUnit.MILLIS,
        listOf("s", "sec", "secs", "second", "seconds") to ChronoUnit.SECONDS,
        listOf("m", "min", "mins", "minute", "minutes") to ChronoUnit.MINUTES,
        listOf("h", "hr", "hrs", "hour", "hours") to ChronoUnit.HOURS,
        listOf("d", "day", "days") to ChronoUnit.DAYS,
        listOf("w", "wk", "wks", "week", "weeks") to ChronoUnit.WEEKS,
        listOf("mo", "mos", "month", "months") to ChronoUnit.MONTHS,
        listOf("y", "yr", "yrs", "year", "years") to ChronoUnit.YEARS,
        // these are definitely needed :3
        listOf("dec", "decs", "decade", "decades") to ChronoUnit.DECADES,
        listOf("cen", "cens", "century", "centuries") to ChronoUnit.CENTURIES,
        listOf("mil", "mils", "millennium", "millennia") to ChronoUnit.MILLENNIA,
        listOf("era", "eras") to ChronoUnit.ERAS
    )

    private val durationSectionRegex = Regex("(\\d*\\.?\\d+)([a-zA-Z]+)", RegexOption.IGNORE_CASE)

    fun parse(unparsed: String): Duration? {
        var duration = Duration.ZERO

        val sections = durationSectionRegex.findAll(unparsed)
        if (sections.count() == 0) return null

        sections.forEach { match ->
            val (multiplier, unit) = match.destructured
            val foundUnit = durationMap.keys.find { it.contains(unit.lowercase()) }
                ?: return null

            val chronoUnit = durationMap[foundUnit]
                ?: return null // shouldn't happen!

            duration = duration.plus(multiplier.toDouble(), chronoUnit)
        }

        return duration
    }

}
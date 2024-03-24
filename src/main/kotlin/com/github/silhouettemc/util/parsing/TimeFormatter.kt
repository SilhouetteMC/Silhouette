package com.github.silhouettemc.util.parsing

import TimeUnit
import java.time.Duration
import java.time.Instant

class TimeFormatter(duration: Duration) {

    val parsableUnits = TimeUnit.entries.reversed()

    var seconds: Long = duration.seconds
    val sb = StringBuilder()

    fun prettify(): String {
        for (unit in parsableUnits) {
            val removedUnits = attemptToRemove(unit, seconds)
            if (removedUnits == 1L) sb.append("$removedUnits ${unit.singular.lowercase()}, ")
            if (removedUnits > 1L) sb.append("$removedUnits ${unit.plural.lowercase()}, ")
        }

        val built = sb.toString().removeSuffix(", ")
        val parts = built.split(", ")
        if (parts.size > 1) {
            val last = parts.last()
            return parts.dropLast(1).joinToString(", ") + " and $last"
        }

        return built
    }

    private fun attemptToRemove(unit: TimeUnit, remaining: Long): Long {
        val unitSeconds = unit.duration.seconds
        if (unitSeconds == 0L) return 0
        val amount = remaining / unitSeconds
        seconds -= amount * unitSeconds
        return amount
    }

}
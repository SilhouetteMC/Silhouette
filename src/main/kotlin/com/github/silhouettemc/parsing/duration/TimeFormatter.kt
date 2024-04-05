package com.github.silhouettemc.parsing.duration

import java.time.Duration

class TimeFormatter(val duration: Duration) {
    val parsableUnits = TimeUnit.entries.reversed()

    var secondsLeft: Long = duration.seconds
    var nanosRemoved: Long = 0

    val sb = StringBuilder()

    fun prettify(round: Boolean = false): String {
        if (secondsLeft == 0L) return "0 seconds"

        val units = if (round) parsableUnits.filter { it.duration.seconds > 0 } else parsableUnits

        for (unit in units) {
            val removedUnits = attemptToRemove(unit, secondsLeft)
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

    private fun attemptToRemove(unit: TimeUnit, remainingSeconds: Long): Long {
        val unitSeconds = unit.duration.seconds

        if (unitSeconds == 0L) {
            val unitNanos = unit.duration.nano
            if (unitNanos <= 0) return 0 // shouldn't happen!

            val newDuration = duration
                .minusSeconds(remainingSeconds)
                .minusNanos(nanosRemoved.toLong())

            val amount = newDuration.nano.toLong() / unitNanos
            nanosRemoved += amount * unitNanos

            return amount
        }

        val amount = remainingSeconds / unitSeconds
        secondsLeft -= amount * unitSeconds
        return amount
    }

}
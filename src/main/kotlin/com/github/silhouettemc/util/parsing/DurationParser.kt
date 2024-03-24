package com.github.silhouettemc.util.parsing

import TimeUnit
import com.github.silhouettemc.util.plus
import java.time.Duration

object DurationParser {

    private val durationSectionRegex = Regex("(\\d*\\.?\\d+)([a-zA-Z]+)", RegexOption.IGNORE_CASE)

    fun parse(unparsed: String): Duration? {
        var duration = Duration.ZERO

        val sections = durationSectionRegex.findAll(unparsed)
        if (sections.count() == 0) return null

        sections.forEach { match ->
            val (multiplier, unit) = match.destructured
            val timeUnit = TimeUnit.entries.find { it.aliases.contains(unit.lowercase()) }
                ?: return null

            duration = duration.plus(multiplier.toDouble(), timeUnit)
        }

        return duration
    }

}
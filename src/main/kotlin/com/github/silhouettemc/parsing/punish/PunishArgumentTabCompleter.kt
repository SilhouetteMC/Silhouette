package com.github.silhouettemc.parsing.punish

import com.github.silhouettemc.parsing.duration.TimeUnit
import co.aikar.commands.BukkitCommandCompletionContext

object PunishArgumentTabCompleter {

    private val primaryDurations = listOf("seconds", "minutes", "hours", "days", "weeks", "months", "years")

    private val alphabetRegex = Regex("[a-zA-Z]")
    private val numberRegex = Regex("\\d*\\.?\\d+")

    private val allowedDurationTags = TimeUnit.entries.map { it.aliases }.flatten()

    fun getFlagCompletions(context: BukkitCommandCompletionContext): List<String> {
        val input = context.input

        if (input.isBlank()) return emptyList()
        if (input.startsWith("-")) return PunishFlag.entries.filter { it.flag.startsWith(input) }.map { it.flag }

        return emptyList()
    }

    fun getDurationAndFlagCompletions(context: BukkitCommandCompletionContext): List<String> {
        val input = context.input

        if (input.isBlank()) return emptyList()
        if (input.startsWith("-")) return PunishFlag.entries.filter { it.flag.startsWith(input) }.map { it.flag }

        val durationTags = input.split(numberRegex)
        val inputDurations = input.split(alphabetRegex)

        val establishedDurationTags = durationTags.dropLast(1)
        for (durationTag in establishedDurationTags) {
            if (durationTag.isEmpty()) continue
            if (!allowedDurationTags.contains(durationTag)) return emptyList()
        }

        val endNumber = inputDurations.last().toDoubleOrNull()
        if (endNumber != null) {
            return primaryDurations.map { "$input$it" }
        }

        val strippedInput = input.removeSuffix(durationTags.last())
        if (strippedInput.isBlank()) return emptyList()

        val matchingDurations = allowedDurationTags.filter { it.startsWith(durationTags.last()) }
        if (matchingDurations.isNotEmpty()) return matchingDurations.map { "$strippedInput$it" }

        return emptyList()
    }


}
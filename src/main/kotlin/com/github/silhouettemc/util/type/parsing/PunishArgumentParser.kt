package com.github.silhouettemc.util.type.parsing

import java.time.Duration


class PunishArgumentParser(
    private val unparsed: String?,
) {

    var reason: String? = null
        private set

    var isSilent: Boolean = false
        private set

    var duration: Duration? = null
        private set

    init {
        if (unparsed != null) attemptToParse(unparsed)
    }

    /**
     * Recursively attempts to parse the endings of the input for flags/etc and removes them, until there are no more to parse.
     */
    private fun attemptToParse(input: String) {
        val endings = getEndings(input)
        println(input)
        if (checkForSilentEndings(input, endings)) return attemptToParse(reason!!)
        if (checkForDuration(input, endings)) return attemptToParse(reason!!)
        checkForNulledReason(input)
    }

    private fun checkForSilentEndings(input: String, endings: Pair<String, String>): Boolean {
        val hasSilentFlags = endings.first.equals("-s", true) || endings.second.equals("-s", true)
        if (hasSilentFlags) isSilent = true
        reason = input.removeEndings("-s")

        return hasSilentFlags
    }

    private fun checkForDuration(input: String, endings: Pair<String, String>): Boolean {
        if (duration != null) return false

        DurationParser.parse(endings.first).let {
            if (it == null) return@let
            duration = it
            reason = input.removeEndings(endings.first)
            return true
        }

        DurationParser.parse(endings.second).let {
            if (it == null) return@let
            duration = it
            reason = input.removeEndings(endings.second)
            return true
        }

        return false
    }

    private fun checkForNulledReason(input: String) {
        if (input.isEmpty()) reason = null
    }

    private fun getEndings(input: String): Pair<String, String> {
        val args = input.split(" ")
        return args.first() to args.last()
    }

    private fun String.removeEndings(ending: String): String {
        return this
            .trim()
            .removePrefix(ending)
            .removeSuffix(ending)
            .trim()
    }

}
package com.github.silhouettemc.util.parsing

import com.github.silhouettemc.util.ConfigUtil
import java.time.Duration


class PunishArgumentParser(
    unparsed: String?,
    private val parseDurations: Boolean = true,
    vararg parsableFlags: PunishFlag
) {

    private val parsableFlags =
        if (parsableFlags.isEmpty()) PunishFlag.entries.toTypedArray() else parsableFlags

    var reason: String? = unparsed
        private set

    var isSilent: Boolean = ConfigUtil.config.getBoolean("punishments.defaultSilent") ?: false
        private set

    var revert: Boolean = false
        private set

    var duration: Duration? = null
        private set

    init {
        reason?.let { attemptToParse() }
    }

    /**
     * Recursively attempts to parse the endings of the input for flags/etc and removes them, until there are no more to parse.
     */
    private fun attemptToParse() {
        val endings = getEndings()

        if (checkForFlag(PunishFlag.SILENT, endings)) {
            isSilent = true
            return attemptToParse()
        }

        if (checkForFlag(PunishFlag.REVERT, endings)) {
            revert = true
            return attemptToParse()
        }

        if (checkForDuration(endings)) return attemptToParse()

        checkForNulledReason()
    }

    private fun checkForFlag(flag: PunishFlag, endings: Pair<String, String>): Boolean {
        if (!parsableFlags.contains(flag)) return false

        val hasFlag = endings.first.equals(flag.flag, true) || endings.second.equals(flag.flag, true)
        reason = reason!!.removeEndings(flag.flag)

        return hasFlag
    }

    private fun checkForDuration(endings: Pair<String, String>): Boolean {
        if (!parseDurations) return false
        if (duration != null) return false

        DurationParser.parse(endings.first).let {
            if (it == null) return@let
            duration = it
            reason = reason!!.removeEndings(endings.first)
            return true
        }

        DurationParser.parse(endings.second).let {
            if (it == null) return@let
            duration = it
            reason = reason!!.removeEndings(endings.second)
            return true
        }

        return false
    }

    private fun checkForNulledReason() {
        if (reason!!.isEmpty()) reason = null
    }

    private fun getEndings(): Pair<String, String> {
        val args = reason!!.split(" ")
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
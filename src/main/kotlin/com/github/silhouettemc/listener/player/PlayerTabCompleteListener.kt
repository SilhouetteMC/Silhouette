package com.github.silhouettemc.listener.player

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.ConfigUtil.getActionBar
import com.github.silhouettemc.parsing.punish.PunishArgumentParser
import com.github.silhouettemc.parsing.duration.TimeFormatter
import com.github.silhouettemc.util.text.sendBar
import com.github.silhouettemc.util.text.translate
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerTabCompleteListener: Listener {

    private val reasonAndDurationTypes = listOf(PunishmentType.BAN, PunishmentType.MUTE)
    private val reasonTypes = listOf(PunishmentType.KICK)

    @EventHandler
    fun AsyncTabCompleteEvent.onTabComplete() {
        if (sender !is Player) return
        val player = sender as Player


        val splits = buffer.split(" ")
        val type = PunishmentType.valueOf(splits[0].removePrefix("/").uppercase())

        if (splits[1].isEmpty()) return player.sendBar("punishment_preparse.noPlayer", mapOf("action" to type.doingName))

        val target = Bukkit.getOfflinePlayerIfCached(splits[1])?.name ?: splits[1]
        val unparsed = splits.drop(2).joinToString(" ")

         if (reasonAndDurationTypes.contains(type)) player.sendReasonAndDurationActionBar(type, target, unparsed)
         if (reasonTypes.contains(type)) player.sendReasonActionBar(type, target, unparsed)
    }

    private fun Player.sendReasonAndDurationActionBar(type: PunishmentType, target: String, unparsed: String) {
        val parsed = PunishArgumentParser(unparsed)

        val reason= parsed.reason ?: "No reason"
        val duration = if (parsed.duration == null) "Forever" else TimeFormatter(parsed.duration!!).prettify()
        val flags = joinFlags(parsed)

        val placeholders = mapOf(
            "action" to type.doingName,
            "duration" to duration,
            "flags" to flags,
            "reason" to reason,
            "target" to target
        )

        val extraPlaceholders = mutableListOf("duration")
        if (flags.isNotEmpty()) extraPlaceholders.add("flags")

        val actionBar = getActionBar("punishment_preparse.base", placeholders, extraPlaceholders)
        this.sendActionBar(translate(actionBar))
    }

    private fun Player.sendReasonActionBar(type: PunishmentType, target: String, unparsed: String) {
        val parsed = PunishArgumentParser(unparsed, parseDurations = false)

        val reason = parsed.reason ?: "No reason"
        val flags = joinFlags(parsed)

        val placeholders = mapOf(
            "action" to type.doingName,
            "reason" to reason,
            "target" to target,
            "flags" to flags,
        )

        val extraPlaceholders = mutableListOf<String>()
        if (flags.isNotEmpty()) extraPlaceholders.add("flags")

        val actionBar = getActionBar("punishment_preparse.base", placeholders, extraPlaceholders)
        this.sendActionBar(translate(actionBar))
    }

    private fun joinFlags(parsed: PunishArgumentParser)
        = parsed.specifiedFlags.joinToString(", ") { it.name.lowercase() }

}
package com.github.silhouettemc.listener.player

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import com.github.silhouettemc.util.parsing.TimeFormatter
import com.github.silhouettemc.util.text.sendTranslated
import com.github.silhouettemc.util.text.translate
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.concurrent.TimeUnit

class PlayerTabCompleteListener: Listener {

    private val reasonAndDurationTypes = listOf(PunishmentType.BAN, PunishmentType.MUTE)
    private val reasonTypes = listOf(PunishmentType.KICK)

    @EventHandler
    fun AsyncTabCompleteEvent.onTabComplete() {
        if (sender !is Player) return
        val player = sender as Player


        val splits = buffer.split(" ")
        val type = PunishmentType.valueOf(splits[0].removePrefix("/").uppercase())

        if (splits[1].isEmpty()) return player.sendActionBar(translate("<p>${type.doingName} <b>|</b> <s>Specify a player!"))

        val target = Bukkit.getOfflinePlayerIfCached(splits[1])?.name ?: splits[1]
        val unparsed = splits.drop(2).joinToString(" ")

         if (reasonAndDurationTypes.contains(type)) player.sendReasonAndDurationActionBar(type, target, unparsed)
         if (reasonTypes.contains(type)) player.sendReasonActionBar(type, target, unparsed)
    }

    private fun Player.sendReasonAndDurationActionBar(type: PunishmentType, target: String, unparsed: String) {
        val parsed = PunishArgumentParser(unparsed)

        val reason= parsed.reason ?: "No reason"
        val duration = if (parsed.duration == null) "Forever" else TimeFormatter(parsed.duration!!).prettify()

        this.sendActionBar(translate("<p>${type.doingName} <s>$target</s> <b>|</b> Reason: <s>$reason</s> <b>|</b> Duration: <s>$duration</s>"))
    }

    private fun Player.sendReasonActionBar(type: PunishmentType, target: String, unparsed: String) {
        val parsed = PunishArgumentParser(unparsed, parseDurations = false)

        val reason = parsed.reason ?: "No reason"

        this.sendActionBar(translate("<p>${type.doingName} <s>$target</s> <b>|</b> Reason: <s>$reason</s>"))
    }

}
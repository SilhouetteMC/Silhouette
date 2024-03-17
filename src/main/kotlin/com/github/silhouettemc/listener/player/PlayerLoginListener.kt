package com.github.silhouettemc.listener.player

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.text.translate
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

object PlayerLoginListener: Listener {

    @EventHandler
    fun AsyncPlayerPreLoginEvent.onPreLogin() {
        // todo: async
        val existingPunishment = Silhouette.getInstance().database.getLatestActivePunishment(uniqueId, PunishmentType.BAN)
            ?: return

        val type = existingPunishment.type
        val expiration = existingPunishment.expiration

        disallow(
            AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
            translate("""
                You are ${type.punishedName}
                Reason: ${existingPunishment.reason}
                Expires: ${expiration?.toString() ?: "Never"}
            """.trimIndent())
        )

    }

}
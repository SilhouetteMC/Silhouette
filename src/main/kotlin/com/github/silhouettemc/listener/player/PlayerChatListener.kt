package com.github.silhouettemc.listener.player

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.command.chat.MuteChatCommand
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.text.sendError
import com.github.silhouettemc.util.text.sendTranslated
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object PlayerChatListener : Listener {

    @EventHandler
    fun AsyncChatEvent.onChat() {
        handleMutedChat()
        handleMutedPlayer()
    }

    private fun AsyncChatEvent.handleMutedPlayer() {
        val existingPunishment = Silhouette.getInstance().database.getLatestActivePunishment(player.uniqueId, PunishmentType.MUTE)
            ?: return

        isCancelled = true

        val formattedMsg = ConfigUtil.getMessage("muted", mapOf(
            "reason" to (existingPunishment.reason ?: "No reason specified"),
            "expiry" to (existingPunishment.expiration?.toString() ?: "Never")
        ))

        player.sendTranslated(formattedMsg)
    }

    private fun AsyncChatEvent.handleMutedChat() {
        if (!MuteChatCommand.isMuted) return // todo: switch this to database access, once implemented
        if (player.hasPermission("silhouettemc.command.mutechat")) return

        isCancelled = true
        player.sendError("The chat is currently muted ;c")
    }

}
package com.github.silhouettemc.listener.player

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.command.chat.MuteChatCommand
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.text.sendError
import com.github.silhouettemc.util.text.sendTranslated
import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.runInterruptible
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerChatListener(
    private val plugin: Silhouette
) : Listener {

    @EventHandler
    fun AsyncChatEvent.onChat() {
        handleMutedChat()
        handleMutedPlayer()
    }

    private fun AsyncChatEvent.handleMutedPlayer() = runBlocking {

        val existingPunishment = plugin.database.getLatestActivePunishment(player.uniqueId, PunishmentType.MUTE)
            ?: return@runBlocking

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
        player.sendError(ConfigUtil.getMessage("errors.chatMuted"))
    }

}
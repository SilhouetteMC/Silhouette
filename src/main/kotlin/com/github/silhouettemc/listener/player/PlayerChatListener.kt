package com.github.silhouettemc.listener.player

import com.github.silhouettemc.command.MuteChatCommand
import com.github.silhouettemc.util.sendError
import com.github.silhouettemc.util.sendTranslated
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object PlayerChatListener : Listener {

    @EventHandler
    fun AsyncChatEvent.onChat() {
        handleMutedChat()
    }

    private fun AsyncChatEvent.handleMutedChat() {
        if (!MuteChatCommand.isMuted) return // todo: switch this to database access, once implemented
        if (player.hasPermission("silhouettemc.command.mutechat")) return

        isCancelled = true
        player.sendError("The chat is currently muted ;c")
    }

}
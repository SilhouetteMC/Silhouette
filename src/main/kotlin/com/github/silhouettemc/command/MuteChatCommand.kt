package com.github.silhouettemc.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.silhouettemc.util.sendError
import com.github.silhouettemc.util.sendTranslated
import com.github.silhouettemc.util.translate
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("mutechat")
@Description("Mutes the chat")
@CommandPermission("silhouettemc.command.mutechat")
object MuteChatCommand : BaseCommand() {

    // todo: this should be stored persistently
    var isMuted = false

    @Default
    fun onCommand(
        sender: CommandSender,
        @Optional muted: Boolean?
    ) {
        val newMuted = muted ?: !isMuted
        val mutedLabel = if (newMuted) "muted" else "unmuted"

        if (isMuted == newMuted)
            return sender.sendError("Chat is already $mutedLabel")

        isMuted = newMuted
        val muter = if (sender is Player) sender.name else "Console"
        val immunityLabel = if (isMuted) ", but you can still talk!" else ""

        for (player in Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("silhouettemc.command.mutechat")) {
                if (player == sender) player.sendMessage(translate("<p>You've <s>$mutedLabel</s> the chat$immunityLabel"))
                else player.sendTranslated("<p>Chat has been <s>$mutedLabel</s> by <s>$muter</s>$immunityLabel")
                continue
            }

            player.sendTranslated("<p>The chat has been <s>$mutedLabel")
        }

        Bukkit.getLogger().info("Chat has been $mutedLabel by $muter")
    }
}
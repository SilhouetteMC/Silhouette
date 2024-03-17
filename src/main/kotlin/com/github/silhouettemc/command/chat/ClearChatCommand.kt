package com.github.silhouettemc.command.chat

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.github.silhouettemc.util.text.sendTranslated
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("clearchat")
@Description("Clears the chat")
@CommandPermission("silhouettemc.command.clearchat")
object ClearChatCommand : BaseCommand() {

    @Default
    fun onCommand(
        sender: CommandSender,
    ) {

        val clearer = if (sender is Player) sender.name else "Console"
        val clearMessage = "\n".repeat(100) + "<p>The chat has been <s>cleared"

        for (player in Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("silhouettemc.command.clearchat")) {
                if (player == sender) player.sendTranslated("<p>You've cleared the chat for others, but you're immune!")
                else player.sendTranslated("<p>The chat has been cleared by <s>$clearer</s>, but you're immune!")
                continue
            }

            player.sendTranslated(clearMessage)
        }

        Bukkit.getLogger().info("Chat has been cleared by $clearer")

    }

}
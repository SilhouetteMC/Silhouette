package com.github.silhouettemc.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.github.silhouettemc.util.translate
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
        val clearMessage = translate("\n".repeat(100) + "<#ffd4e3>The chat has been <#ffb5cf>cleared")
        val staffMessage = translate("<#ffd4e3>The chat has been cleared by <#ffb5cf>$clearer</#ffb5cf>, but you're immune!")

        for (player in Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("silhouettemc.command.clearchat")) {
                if (player == sender) player.sendMessage(translate("<#ffd4e3>You've cleared the chat for others, but you're immune!"))
                else player.sendMessage(staffMessage)
                continue
            }

            player.sendMessage(clearMessage)
        }

        Bukkit.getLogger().info("Chat has been cleared by $clearer")

    }

}
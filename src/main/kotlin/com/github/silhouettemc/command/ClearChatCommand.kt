package com.github.silhouettemc.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.github.silhouettemc.Silhouette.Companion.mm
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("clearchat")
@Description("Clears the chat.")
@CommandPermission("silhouettemc.command.clearchat")
object ClearChatCommand : BaseCommand() {

    @Default
    fun onCommand(
        sender: CommandSender,
    ) {

        val clearer = if (sender is Player) sender.name else "Console"
        val clearMessage = mm.deserialize("\n".repeat(100) + "<#ffa8e8>Chat has been cleared")
        val staffMessage = mm.deserialize("<#ffa8e8>Chat has been cleared by <#ff80dd>$clearer")

        for (player in Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("silhouettemc.command.clearchat")) {
                player.sendMessage(staffMessage)
                continue
            }

            player.sendMessage(clearMessage)
        }

        Bukkit.getLogger().info("Chat has been cleared by $clearer")

    }

}
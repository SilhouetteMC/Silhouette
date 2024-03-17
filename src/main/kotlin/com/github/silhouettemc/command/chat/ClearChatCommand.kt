package com.github.silhouettemc.command.chat

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.github.silhouettemc.util.ConfigUtil
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

        val placeholders = mapOf(
            "clearer" to clearer
        )

        val alertAll = ConfigUtil.getMessage("commands.clearchat.alertAll", placeholders)
        val alertSelf = ConfigUtil.getMessage("commands.clearchat.alertSelf", placeholders)
        val alertStaff = ConfigUtil.getMessage("commands.clearchat.alertStaff", placeholders)

        val clearMessage = "\n".repeat(100) + alertAll

        for (player in Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("silhouettemc.command.clearchat")) {
                if (player == sender) player.sendTranslated(alertSelf)
                else player.sendTranslated(alertStaff)
                continue
            }

            player.sendTranslated(clearMessage)
        }

        Bukkit.getLogger().info("Chat has been cleared by $clearer")

    }

}
package com.github.silhouettemc.command.plugin

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.text.sendTranslated
import org.bukkit.command.CommandSender

// todo: make this a sub command somehow idk
@CommandAlias("silhouette")
@Description("Reloads the Silhouette configuration")
@CommandPermission("silhouettemc.command.reload")
object ReloadCommand : BaseCommand() {
    @Default
    fun onCommand(
        sender: CommandSender,
    ) {

        ConfigUtil.load()
        sender.sendTranslated(ConfigUtil.getMessage("general.reloadConfig"))

    }

}
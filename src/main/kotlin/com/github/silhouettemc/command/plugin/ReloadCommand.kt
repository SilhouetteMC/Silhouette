package com.github.silhouettemc.command.plugin

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.text.send
import com.github.silhouettemc.util.text.sendTranslated
import org.bukkit.command.CommandSender

@CommandAlias("silhouette")
@Description("Reloads the Silhouette configuration")
object ReloadCommand : BaseCommand() {

    @Default
    @Subcommand("info")
    fun info(sender: CommandSender) {
        // todo: actual info, also maybe this should be configurable for colors..?
        sender.sendTranslated("""
            <p><s>Silhouette</s> info blah blah blah</p>
            <p>Type <s>/silhouette help</s> for help!
        """.trimIndent())
    }

    @Subcommand("reload")
    @CommandPermission("silhouette.command.reload")
    fun reload(sender: CommandSender) {
        ConfigUtil.load()
        sender.send("reloadConfig")
    }

    // todo: subcommands: help, version, info

}
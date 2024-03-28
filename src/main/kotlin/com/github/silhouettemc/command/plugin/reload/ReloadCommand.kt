package com.github.silhouettemc.command.plugin.reload

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.text.getCenteredMessage
import com.github.silhouettemc.util.text.send
import com.github.silhouettemc.util.text.sendTranslated
import org.bukkit.command.CommandSender

@CommandAlias("silhouette")
@Description("Reloads the Silhouette configuration")
object ReloadCommand : BaseCommand() {

    @Default
    @Subcommand("info")
    fun info(sender: CommandSender) {

        // todo: make buttons work
        // todo: tertiary color (#6b5569) to be configgable?

        val info = KittyCatBuilder()
            .setLine(1, getCenteredMessage("<s><b>»</b><#6b5569><st>                           </st><s><b>«", 71))
            .setLine(3, getCenteredMessage("<p><b>Silhouette :3", 74))

            .setLine(5, getCenteredMessage("<s><i>An open sourced, feature", 73))
            .setLine(6, getCenteredMessage("<s><i>packed yet beautifully", 73))
            .setLine(7, getCenteredMessage("<s><i>simple moderation system.", 73))
            .setLine(8, getCenteredMessage("<s><i><u>/silhouette help</u>", 73))


            .setLine(10, getCenteredMessage("<p><b><u>Docs</u></b> <#6b5569>| <p><b><u>Discord</u></b>", 70))

            .setLine(12, getCenteredMessage("<s>❤", 72))

            .setLine(14, getCenteredMessage("<s><b>»</b><#6b5569><st>                           </st><s><b>«", 71))
            .build()

        sender.sendMessage("")
        sender.sendMessage(info)
        sender.sendMessage("")
    }

    @Subcommand("reload")
    @CommandPermission("silhouette.command.reload")
    fun reload(sender: CommandSender) {
        ConfigUtil.load()
        sender.send("reloadConfig")
    }

    // todo: subcommands: help, version, info

}
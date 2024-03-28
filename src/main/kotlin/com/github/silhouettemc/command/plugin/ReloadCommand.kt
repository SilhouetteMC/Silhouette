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
        // todo: actual info

        sender.sendTranslated("""
            
            | xxxxxxxxxxxxxxxxxxx
            | xxxBBxxxxxxxxxBBxxx
            | xxxBLBxxxxxxxBLBxxx
            | xxxBPLBBBBBBBLPBxxx
            | xxxBPLLDLDLDLLPBxxx
            | xxxBLLLDLDLDLLLBxxx
            | xxxBLLLLLDLLLLLLBxx
            | xxBLLLLBLLLLLBLLBxx
            | xBBDLLLBLLLLLBLDBBx
            | xxBLLLLLLLLLLLLLBxx
            | xBBDLLLLPPPLLLLDBBx
            | xxBLLLLLLPLLLLLLBxx
            | xxxBLLLLLLLLLLLBxxx
            | xxxxBBLDLLLDLBBxxxx
            | xxxxxxBBBBBBBxxxxxx
            | xxxxxxxxxxxxxxxxxxx
            
        """
            .trimMargin()
            .replace("x", "<#cc8da7>█</#cc8da7>") // background
            .replace("B", "<#323236>█</#323236>") // border
            .replace("P", "<#fbced7>█</#fbced7>") // pink
            .replace("L", "<#cfcfcf>█</#cfcfcf>") // light gray
            .replace("D", "<#8d8d8d>█</#8d8d8d>") // dark gray
        )
    }

    @Subcommand("reload")
    @CommandPermission("silhouette.command.reload")
    fun reload(sender: CommandSender) {
        ConfigUtil.load()
        sender.send("reloadConfig")
    }

    // todo: subcommands: help, version, info

}
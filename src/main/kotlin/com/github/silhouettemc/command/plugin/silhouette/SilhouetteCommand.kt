package com.github.silhouettemc.command.plugin.silhouette

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.text.getCenteredMessage
import com.github.silhouettemc.util.text.send
import org.bukkit.command.CommandSender

@CommandAlias("silhouette")
@Description("Displays info about Silhouette")
object SilhouetteCommand : BaseCommand() {

    @Default
    @Subcommand("info")
    fun info(sender: CommandSender) {

        val aroze = personalSiteButton("Aroze", "https://aroze.me")
        val astrid = personalSiteButton("Astrid", "https://astrid.sh")
        val eva = personalSiteButton("Eva", "https://kibty.town")

        // todo: real docs link, real discord link
        val docs = button("Docs", "https://github.com/SilhouetteMC/Silhouette/wiki",
            "<s><i>Click to view Silhouette\\'s documentation!\n" +
                "<p>➥ Learn about our features, see examples, FAQ\\'s, and more!"
        )

        val discord = button("Discord", "https://discord.gg",
            "<s><i>Click to join the Silhouette Discord server!\n" +
            "<p>➥ Community support, announcements, make suggestions c:"
        )

        val info = KittyCatBuilder()
            .setLine(1, getCenteredMessage("<s><b>»</b><t><st>                           </st><s><b>«", 71))
            .setLine(3, getCenteredMessage("<p><b>Silhouette :3", 74))

            .setLine(5, getCenteredMessage("<s><i>An open sourced, feature", 73))
            .setLine(6, getCenteredMessage("<s><i>packed yet beautifully", 73))
            .setLine(7, getCenteredMessage("<s><i>simple moderation system.", 73))
            .setLine(8, getCenteredMessage("<s><i>Authors: $aroze $astrid $eva", 73))

            .setLine(10, getCenteredMessage("$docs <t>| $discord", 70))

            .setLine(12, getCenteredMessage("<s>v${Silhouette.getInstance().pluginMeta.version} ❤", 72))

            .setLine(14, getCenteredMessage("<s><b>»</b><t><st>                           </st><s><b>«", 71))
            .build()

        sender.sendMessage("")
        sender.sendMessage(info)
        sender.sendMessage("")
    }

    @Subcommand("reload")
    @CommandPermission("silhouette.command.reload")
    @Description("Reloads Silhouette's config")
    fun reload(sender: CommandSender) {
        ConfigUtil.load()
        sender.send("reloadConfig")
    }

    private fun personalSiteButton(name: String, url: String) =
        "<click:open_url:$url><hover:show_text:'<i><s><u>${url}/</u></s> <t><b>|</b></t> <p>Click to visit ❤</p></i>'><u>${name}</u></hover></click>"

    private fun button(name: String, url: String, hover: String) =
        "<click:open_url:$url><hover:show_text:'$hover'><p><b><u>${name}</u></b></p></hover></click>"

}
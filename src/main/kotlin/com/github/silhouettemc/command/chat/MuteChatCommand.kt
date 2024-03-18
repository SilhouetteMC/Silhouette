package com.github.silhouettemc.command.chat

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.text.sendError
import com.github.silhouettemc.util.text.sendTranslated
import com.github.silhouettemc.util.text.translate
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
        val muter = if (sender is Player) sender.name else "Console"

        val placeholders = mapOf(
            "state" to mutedLabel,
            "player" to muter
        )

        if (isMuted == newMuted) {
            return sender.sendError(ConfigUtil.getMessage("mutechat.invalidAction"))
        }

        isMuted = newMuted

        val alertSelf = ConfigUtil.getMessage("mutechat.${mutedLabel}AlertSelf", placeholders)
        val alertStaff = ConfigUtil.getMessage("mutechat.${mutedLabel}AlertStaff", placeholders)

        for (player in Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("silhouettemc.command.mutechat")) {
                if (player == sender) player.sendMessage(translate(alertSelf))
                else player.sendTranslated(alertStaff)
                continue
            }

            val msg = ConfigUtil.getMessage("mutechat.alertAll", placeholders)
            player.sendTranslated(msg)
        }

        Bukkit.getLogger().info("Chat has been $mutedLabel by $muter")
    }
}
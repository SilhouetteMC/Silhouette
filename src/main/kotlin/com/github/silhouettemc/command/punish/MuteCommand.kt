package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Flags
import co.aikar.commands.annotation.Optional
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.parsing.PlayerProfileRetriever
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import com.github.silhouettemc.util.text.send
import org.bukkit.entity.Player
import java.time.Instant

@CommandAlias("mute")
@Description("Mutes a player")
@CommandPermission("silhouette.punish.mute")
object MuteCommand : BaseCommand() {

    @Default
    @CommandCompletion("@players @punish_args")
    fun onCommand(
        sender: Player,
        @Flags("other") retriever: PlayerProfileRetriever,
        @Optional unparsed: String?,
    ) {
        val placeholders = mapOf(
            "player" to retriever.name,
            "existing-action" to PunishmentType.MUTE.actionName.lowercase()
        )

        val player = retriever.fetchOfflinePlayerProfile()
            ?: return sender.send("errors.noPlayerFound", placeholders)
        val playerUUID = player.id!!

        val args = PunishArgumentParser(unparsed)

        if(!args.override) {
            val existingPunishment = Silhouette.getInstance().database.hasActivePunishment(playerUUID, PunishmentType.MUTE)
            if (existingPunishment) {
                return sender.send("errors.existingPunishment", placeholders)
            }
        }

        val expiry = args.duration?.let { Instant.now().plus(it) }

        Punishment(
            playerUUID,
            Actor(sender.uniqueId),
            args.reason,
            PunishmentType.MUTE,
            expiry
        ).process(args)
    }
}
package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.parsing.PlayerProfileRetriever
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import com.github.silhouettemc.util.text.send
import com.github.silhouettemc.util.text.sendError
import org.bukkit.entity.Player
import java.time.Instant

@CommandAlias("ban")
@Description("Bans a player")
@CommandPermission("silhouette.punish.ban")
object BanCommand : BaseCommand() {

    @Default
    @CommandCompletion("@players @punish_args")
    fun onCommand(
        sender: Player,
        @Flags("other") retriever: PlayerProfileRetriever,
        @Optional unparsed: String?,
    ) {
        val placeholders = mapOf(
            "player" to retriever.name,
            "existing-action" to PunishmentType.BAN.actionName.lowercase()
        )

        val player = retriever.fetchOfflinePlayerProfile()
            ?: return sender.send("errors.noPlayerFound", placeholders)
        val playerUUID = player.id!!

        val args = PunishArgumentParser(unparsed)
        if(!args.override && !args.replace) {
            val existingPunishment = Silhouette.getInstance().database.hasActivePunishment(playerUUID, PunishmentType.BAN)
            if(existingPunishment) {
                return sender.send("errors.existingPunishment", placeholders)
            }
        }

        val expiry = args.duration?.let { Instant.now().plus(it) }

        Punishment(
            playerUUID,
            Actor(sender.uniqueId),
            args.reason,
            PunishmentType.BAN,
            expiry
        ).process(args)
    }
}
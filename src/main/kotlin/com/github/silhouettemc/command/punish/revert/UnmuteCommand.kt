package com.github.silhouettemc.command.punish.revert

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.parsing.PlayerProfileRetriever
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import com.github.silhouettemc.util.text.send
import org.bukkit.entity.Player

@CommandAlias("unmute")
@Description("Unmute a player")
@CommandPermission("silhouette.punish.unmute")
object UnmuteCommand : BaseCommand() {

    @Default
    @CommandCompletion("@players @punish_flags")
    fun onCommand(
        sender: Player,
        @Flags("other") retriever: PlayerProfileRetriever,
        @Optional unparsed: String?,
    ) {
        val placeholders = mutableMapOf(
            "player" to retriever.name,
            "existing-action" to PunishmentType.MUTE.punishedName.lowercase()
        )

        val player = retriever.fetchOfflinePlayerProfile()
            ?: return sender.send("errors.noPlayerFound", placeholders)

        val existingPunishment = Silhouette.getInstance().database.getLatestActivePunishment(player.id!!, PunishmentType.MUTE)
            ?: return sender.send("errors.noExistingPunishment", placeholders)

        val args = PunishArgumentParser(unparsed, false)
        existingPunishment.revert(Actor(sender.uniqueId), args)
    }
}
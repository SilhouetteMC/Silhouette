package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.parsing.PlayerProfileRetriever
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import com.github.silhouettemc.util.text.send
import org.bukkit.entity.Player

@CommandAlias("unban")
@Description("Unbans a player")
@CommandPermission("silhouette.punish.unban")
object UnbanCommand : BaseCommand() {
    @Default
    @CommandCompletion("@players @punish_args")
    fun onCommand(
        sender: Player,
        @Flags("other") retriever: PlayerProfileRetriever,
        @Optional unparsed: String?,
    ) {
        val placeholders = mutableMapOf(
            "player" to retriever.name,
            "existing-action" to PunishmentType.BAN.punishedName.lowercase()
        )

        val player = retriever.fetchOfflinePlayerProfile()
            ?: return sender.send("errors.noPlayerFound", placeholders)

        val existingPunishment = Silhouette.getInstance().database.getLatestActivePunishment(player.id!!, PunishmentType.BAN)
            ?: return sender.send("errors.noExistingPunishment", placeholders)

        val args = PunishArgumentParser(unparsed)
        existingPunishment.revert(Actor(sender.uniqueId), args)
    }
}
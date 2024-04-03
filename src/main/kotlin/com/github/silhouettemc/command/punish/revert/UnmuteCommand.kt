package com.github.silhouettemc.command.punish.revert

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.parsing.PlayerProfileRetriever
import com.github.silhouettemc.parsing.punish.PunishArgumentParser
import com.github.silhouettemc.util.text.send
import org.bukkit.entity.Player

@CommandAlias("unmute")
@Description("Unmute a player")
@CommandPermission("silhouette.punish.unmute")
object UnmuteCommand : BaseCommand() {

    @Dependency
    lateinit var plugin: Silhouette

    @Default
    @CommandCompletion("@players @punish_flags")
    fun onCommand(
        sender: Player,
        @Flags("other") retriever: PlayerProfileRetriever,
        @Optional unparsed: String?,
    ) = UnmuteCommand.plugin.launch(plugin.asyncDispatcher) {

        val placeholders = mutableMapOf(
            "target" to retriever.name,
            "existing-action" to PunishmentType.MUTE.punishedName.lowercase()
        )

        val player = retriever.fetchOfflinePlayerProfile()
            ?: return@launch sender.send("errors.noPlayerFound", placeholders)

        val existingPunishment = Silhouette.getInstance().database.getLatestActivePunishment(player.id!!, PunishmentType.MUTE)
            ?: return@launch sender.send("errors.noExistingPunishment", placeholders)

        val args = PunishArgumentParser(unparsed, false)
        existingPunishment.revert(Actor(sender.uniqueId), args)
    }
}
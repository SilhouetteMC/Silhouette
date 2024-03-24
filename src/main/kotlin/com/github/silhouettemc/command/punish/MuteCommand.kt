package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.parsing.PlayerProfileRetriever
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import com.github.silhouettemc.util.text.send
import org.bukkit.entity.Player
import java.time.Instant

@CommandAlias("mute")
@Description("Mutes a player")
@CommandPermission("silhouette.punish.mute")
object MuteCommand : BaseCommand() {

    @Dependency
    lateinit var plugin: Silhouette

    @Default
    @CommandCompletion("@players @punish_args")
    fun onCommand(
        sender: Player,
        @Flags("other") retriever: PlayerProfileRetriever,
        @Optional unparsed: String?,
    ) = plugin.launch(plugin.asyncDispatcher) {

        val placeholders = mapOf(
            "target" to retriever.name
        )

        val player = retriever.fetchOfflinePlayerProfile()
            ?: return@launch sender.send("errors.noPlayerFound", placeholders)

        val args = PunishArgumentParser(unparsed)
        val expiry = args.duration?.let { Instant.now().plus(it) }

        Punishment(
            player.id!!,
            Actor(sender.uniqueId),
            args.reason,
            PunishmentType.MUTE,
            expiry
        ).process(args)
    }
}
package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Flags
import co.aikar.commands.annotation.Optional
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.text.sendError
import com.github.silhouettemc.util.parsing.PlayerProfileRetriever
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import org.bukkit.entity.Player
import java.time.Instant

@CommandAlias("mute")
@Description("Mutes a player")
@CommandPermission("silhouettemc.punish.mute")
object MuteCommand : BaseCommand() {

    @Default
    @CommandCompletion("@players @punish_args")
    fun onCommand(
        sender: Player,
        @Flags("other") retriever: PlayerProfileRetriever,
        @Optional unparsed: String?,
    ) {
        val player = retriever.fetchOfflinePlayerProfile()
            ?: return sender.sendError("Couldn't find a player called ${retriever.name} ;c")

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
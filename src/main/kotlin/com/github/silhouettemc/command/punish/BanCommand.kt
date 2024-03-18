package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.text.sendError
import com.github.silhouettemc.util.parsing.PlayerProfileRetriever
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import org.bukkit.entity.Player
import java.time.Instant

@CommandAlias("ban")
@Description("Bans a player")
@CommandPermission("silhouettemc.punish.ban")
object BanCommand : BaseCommand() {

    @Default
    @CommandCompletion("@players @punish_args")
    fun onCommand(
        sender: Player,
        @Flags("other") retriever: PlayerProfileRetriever,
        @Optional unparsed: String?,
    ) {
        val placeholders = mapOf(
            "player" to retriever.name
        )

        val player = retriever.fetchOfflinePlayerProfile()
            ?: return sender.sendError(ConfigUtil.getMessage("general.noPlayerFound", placeholders))

        val args = PunishArgumentParser(unparsed)
        val expiry = args.duration?.let { Instant.now().plus(it) }

        Punishment(
            player.id!!,
            Actor(sender.uniqueId),
            args.reason,
            PunishmentType.BAN,
            expiry
        ).process(args)
    }
}
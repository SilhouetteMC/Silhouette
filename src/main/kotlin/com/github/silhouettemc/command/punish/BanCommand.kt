package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Flags
import co.aikar.commands.annotation.Optional
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.sendError
import com.github.silhouettemc.util.sendTranslated
import com.github.silhouettemc.util.type.PlayerProfileRetriever
import com.github.silhouettemc.util.type.ReasonContext
import org.bukkit.entity.Player
import java.time.Instant
import java.util.Date

@CommandAlias("ban")
@Description("Bans a player")
@CommandPermission("silhouettemc.punish.ban")
object BanCommand : BaseCommand() {

    @Default
    fun onCommand(
        sender: Player,
        @Flags("other") retriever: PlayerProfileRetriever,
        @Optional reason: String?,
    ) {

        val player = retriever.fetchOfflinePlayerProfile()
            ?: return sender.sendError("Couldn't find a player called ${retriever.name} ;c")

        val reasonContext = ReasonContext(reason)

        Punishment(
            player.id!!,
            Actor(sender.uniqueId),
            reasonContext.reason,
            PunishmentType.BAN,
        ).process(reasonContext)

    }
}
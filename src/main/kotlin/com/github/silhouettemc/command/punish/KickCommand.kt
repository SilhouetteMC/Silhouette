package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Flags
import co.aikar.commands.annotation.Optional
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import org.bukkit.entity.Player

@CommandAlias("kick")
@Description("Kicks a player")
@CommandPermission("silhouettemc.punish.kick")
object KickCommand : BaseCommand() {

    @Default
    fun onCommand(
        sender: Player,
        @Flags("other") player: Player,
        @Optional reason: String?,
    ) {

        Punishment(
            player.uniqueId,
            Actor(sender.uniqueId),
            reason,
            PunishmentType.KICK,
        ).process()

    }
}
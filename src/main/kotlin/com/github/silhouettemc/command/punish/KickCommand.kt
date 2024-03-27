package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import org.bukkit.entity.Player

@CommandAlias("kick")
@Description("Kicks a player")
@CommandPermission("silhouette.punish.kick")
object KickCommand : BaseCommand() {

    @Dependency
    lateinit var plugin: Silhouette

    @Default
    @CommandCompletion("@players @punish_flags")
    fun onCommand(
        sender: Player,
        @Flags("other") player: Player,
        @Optional unparsed: String?,
    ) = plugin.launch(plugin.asyncDispatcher) {
        val args = PunishArgumentParser(unparsed)

        Punishment.withArgs(
            player.playerProfile,
            Actor(sender.uniqueId),
            args,
            PunishmentType.KICK,
        ).process(args)
    }
}
package com.github.silhouettemc.plugin.punishment

import com.github.silhouettemc.plugin.actor.Actor
import com.github.silhouettemc.plugin.actor.player.DataPlayer
import java.time.Instant
import java.util.Date

data class Punishment(
    val player: DataPlayer,
    val actor: Actor,
    val type: PunishmentType,
    val expiration: Instant? = null,
    val punishedOn: Date = Date()
)

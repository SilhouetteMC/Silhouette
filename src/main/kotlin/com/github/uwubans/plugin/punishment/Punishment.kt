package com.github.uwubans.plugin.punishment

import com.github.uwubans.plugin.actor.Actor
import com.github.uwubans.plugin.actor.player.DataPlayer
import java.time.Instant
import java.util.Date

data class Punishment(
        val player: DataPlayer,
        val actor: Actor,
        val type: PunishmentType,
        val expiration: Instant? = null,
        val punishedOn: Date = Date()
)

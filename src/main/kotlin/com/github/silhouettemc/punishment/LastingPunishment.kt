package com.github.silhouettemc.punishment

import com.github.silhouettemc.actor.Actor
import java.time.Instant
import java.util.Date
import java.util.UUID

class LastingPunishment (
    id: UUID,
    player: UUID,
    punisher: Actor,
    reason: String? = null,
    type: PunishmentType,
    punishedOn: Date = Date(),

    val expiration: Instant? = null,
    val revoker: Actor? = null,
    val revokeReason: String? = null,
) : Punishment(player, punisher, reason, type, id, punishedOn)

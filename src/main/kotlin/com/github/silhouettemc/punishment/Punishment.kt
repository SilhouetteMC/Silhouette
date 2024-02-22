package com.github.silhouettemc.punishment

import com.github.silhouettemc.actor.Actor
import java.util.*

open class Punishment(
    val player: UUID,
    val punisher: Actor,
    val reason: String? = null,
    val type: PunishmentType,
    val punishedOn: Date = Date()
) {

    fun process() {
        TODO()
    }

}

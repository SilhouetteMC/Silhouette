package com.github.silhouettemc.actor.player

import com.github.silhouettemc.actor.Actor
import java.util.UUID

open class Player(val uuid: UUID): Actor(uuid) {
    fun getPlayerName(): String {
        TODO()
    }
}
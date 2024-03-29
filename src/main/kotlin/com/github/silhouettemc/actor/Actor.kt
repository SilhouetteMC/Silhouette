package com.github.silhouettemc.actor

import com.github.silhouettemc.actor.player.Player
import java.util.*

val CONSOLE_ID = UUID(0, 0)
open class Actor(val id: UUID = CONSOLE_ID) {
    fun getReadableName(): String {
        if (this is Player) {
            return this.getPlayerName()
        }

        return "Console"
    }
}
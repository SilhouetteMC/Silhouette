package com.github.uwubans.plugin.actor

import com.github.uwubans.plugin.actor.player.Player
import java.util.UUID

val CONSOLE_ID = UUID(0, 0)
open class Actor(val id: UUID = CONSOLE_ID) {
    fun getReadableName(): String {
        if (this is Player) {
            return this.getPlayerName()
        }

        return "Console"
    }
}
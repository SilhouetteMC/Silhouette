package com.github.uwubans.plugin.actor.player

import com.github.uwubans.plugin.actor.Actor
import java.util.UUID

open class Player(val uuid: UUID): Actor(uuid) {
    fun getPlayerName(): String {
        TODO()
    }
}
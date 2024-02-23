package com.github.silhouettemc.database

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.punishment.Punishment
import java.util.*

interface Database {
    fun initialize(plugin: Silhouette)

    fun addPunishment(punishment: Punishment)

    fun updatePunishment(punishment: Punishment)

    fun removePunishment(punishment: Punishment)

    fun listPunishments(player: UUID): List<Punishment>
}
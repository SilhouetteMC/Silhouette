package com.github.silhouettemc.database

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import org.bson.conversions.Bson
import java.util.*

interface Database {
    suspend fun initialize(plugin: Silhouette)

    suspend fun addPunishment(punishment: Punishment)

    suspend fun updatePunishment(punishment: Punishment, vararg updates: Bson)

    suspend fun removePunishment(punishment: Punishment)

    suspend fun listPunishments(player: UUID): List<Punishment>

    suspend fun getLatestActivePunishment(player: UUID, type: PunishmentType): Punishment?

    // todo: disconnect()?
}
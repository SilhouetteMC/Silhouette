package com.github.silhouettemc.database

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.history.History
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import org.bson.conversions.Bson
import java.util.*

interface Database {
    suspend fun initialize(plugin: Silhouette)

    suspend fun addPunishment(punishment: Punishment)

    suspend fun addHistory(history: History)
    suspend fun getHistory(punishmentId: UUID): List<History>

    suspend fun updatePunishment(punishment: Punishment, vararg updates: Bson)

    suspend fun removePunishment(punishment: Punishment)

    suspend fun listPunishments(player: UUID): List<Punishment>
    suspend fun listPunishments(player: UUID, type: PunishmentType): List<Punishment>

    suspend fun getLatestActivePunishment(player: UUID, type: PunishmentType): Punishment?

    // todo: disconnect()?
}
package com.github.silhouettemc.database

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import org.bson.conversions.Bson
import java.util.*

interface Database {
    fun initialize(plugin: Silhouette)

    fun addPunishment(punishment: Punishment)

    fun updatePunishment(punishment: Punishment, vararg updates: Bson)

    fun removePunishment(punishment: Punishment)

    fun listPunishments(player: UUID): List<Punishment>

    fun hasActivePunishment(player: UUID, type: PunishmentType): Boolean

    fun getLatestActivePunishment(player: UUID, type: PunishmentType): Punishment?

    // todo: disconnect()?
}
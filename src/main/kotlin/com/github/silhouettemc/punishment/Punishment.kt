package com.github.silhouettemc.punishment

import com.github.silhouettemc.actor.Actor
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

@DatabaseTable(tableName = "punishments")
open class Punishment(
    @DatabaseField(canBeNull = false, generatedId = true)
    val id: UUID,

    @DatabaseField(canBeNull = false)
    val player: UUID,
    @DatabaseField(canBeNull = false, dataType = DataType.SERIALIZABLE)
    val punisher: Actor,
    @DatabaseField(canBeNull = true)
    val reason: String? = null,
    @DatabaseField(canBeNull = false)
    val type: PunishmentType,
    val punishedOn: Date = Date()
) {

    fun process() {
        TODO()
    }

}

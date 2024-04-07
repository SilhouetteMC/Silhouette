package com.github.silhouettemc.history

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

data class Update(
    val current: String?,
    val prior: String?,
)

@DatabaseTable(tableName = "history")
data class History(
    @DatabaseField(canBeNull = false)
    val punishmentId: UUID,

    @DatabaseField(canBeNull = true, dataType = DataType.SERIALIZABLE)
    val reason: Update? = null,

    @DatabaseField(canBeNull = true, dataType = DataType.SERIALIZABLE)
    val duration: Update? = null,

    @DatabaseField(canBeNull = false)
    var createdOn: Date = Date()
)
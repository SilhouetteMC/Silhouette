package com.github.silhouettemc.punishment

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.Silhouette.Companion.mm
import com.github.silhouettemc.actor.Actor
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import org.bukkit.Bukkit
import java.util.*

@DatabaseTable(tableName = "punishments")
open class Punishment(
    @DatabaseField(canBeNull = false)
    val player: UUID,
    @DatabaseField(canBeNull = false, dataType = DataType.SERIALIZABLE)
    val punisher: Actor,
    @DatabaseField(canBeNull = true)
    val reason: String? = null,
    @DatabaseField(canBeNull = false)
    val type: PunishmentType,

    @DatabaseField(canBeNull = false)
    val id: UUID = UUID.randomUUID(),

    @DatabaseField(canBeNull = false)
    val punishedOn: Date = Date()
) {

    fun process() {
        Silhouette.getInstance().database.addPunishment(this)

        handleDisconnect()
    }

    private fun handleDisconnect() {
        if (type.shouldDisconnect) {
            val player = Bukkit.getPlayer(player) ?: return

            if (reason == null) {
                player.kick(mm.deserialize("You have been ${type.punishedName}"))
                return
            }

            player.kick(
                mm.deserialize(
                    """
                        You have been ${type.punishedName}
                        Reason: $reason
                    """.trimIndent()
                )
            )

        }
    }

}

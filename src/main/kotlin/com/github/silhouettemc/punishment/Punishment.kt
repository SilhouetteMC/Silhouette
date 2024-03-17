package com.github.silhouettemc.punishment

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.Silhouette.Companion.mm
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.util.text.translate
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.bukkit.Bukkit
import java.time.Instant
import java.util.*

@DatabaseTable(tableName = "punishments")
data class Punishment(
    @DatabaseField(canBeNull = false)
    val player: UUID,
    @DatabaseField(canBeNull = false, dataType = DataType.SERIALIZABLE)
    val punisher: Actor,
    @DatabaseField(canBeNull = true)
    val reason: String? = null,
    @DatabaseField(canBeNull = false)
    val type: PunishmentType,

    val expiration: Instant? = null,
    val revoker: Actor? = null,
    val revokeReason: String? = null,

    @DatabaseField(canBeNull = false)
    val id: UUID = UUID.randomUUID(),

    @DatabaseField(canBeNull = false)
    val punishedOn: Date = Date()
) {

    fun process(args: PunishArgumentParser) {
        Silhouette.getInstance().database.addPunishment(this) // todo: async

        if (type.shouldDisconnect) handleDisconnect()
        if (!args.isSilent) broadcastPunishment()
    }

    private fun handleDisconnect() {
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

    private fun broadcastPunishment() {
        Bukkit.broadcast(
            translate(
            """
                <p>$player was <s>${type.punishedName}</s> by <s>${punisher.getReadableName()}</s>
            """.trimIndent()
        )
        )
    }

    @get:BsonIgnore
    val isExpired
        get() = expiration?.isBefore(Instant.now()) ?: false

}

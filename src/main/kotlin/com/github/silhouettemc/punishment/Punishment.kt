package com.github.silhouettemc.punishment

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.parsing.PunishArgumentParser
import com.github.silhouettemc.util.text.translate
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.mongodb.client.model.Updates
import kotlinx.coroutines.withContext
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
    val revokedAt: Date? = null,

    @DatabaseField(canBeNull = false)
    val id: UUID = UUID.randomUUID(),

    @DatabaseField(canBeNull = false)
    val punishedOn: Date = Date()
) {

    suspend fun process(args: PunishArgumentParser) {
        Silhouette.getInstance().database.addPunishment(this)

        if (type.shouldDisconnect) handleDisconnect()
        if (!args.isSilent) broadcastPunishment()
    }

    suspend fun revert(revoker: Actor, args: PunishArgumentParser) {
        Silhouette.getInstance().database.updatePunishment(
            this,
            Updates.set(Punishment::revoker.name, revoker),
            Updates.set(Punishment::revokeReason.name, args.reason),
            Updates.set(Punishment::revokedAt.name, Date())
        )

        val type = when(type) {
            PunishmentType.BAN -> PunishmentType.UNBAN
            PunishmentType.MUTE -> PunishmentType.UNMUTE
            else -> PunishmentType.UNBAN
        }

        if (!args.isSilent) broadcastPunishment(revoker, type)
    }

    private suspend fun handleDisconnect() {
        val player = Bukkit.getPlayer(player) ?: return

        val placeholders = mapOf(
            "player" to player.toString(),
            "punisher" to punisher.getReadableName(),
            "reason" to (reason ?: "No reason specified"),
            "action" to type.punishedName
        )

        val msg = ConfigUtil.getMessage("${type.actionName.lowercase()}Screen", placeholders)

        withContext(Silhouette.getInstance().minecraftDispatcher) {
            player.kick(translate(msg))
        }
    }

    private fun broadcastPunishment(actor: Actor, type: PunishmentType) {
        val broadcast = ConfigUtil.getMessage("broadcast", mapOf(
            "player" to player.toString(),
            "action" to type.punishedName,
            "punisher" to actor.getReadableName()
        ))
        Bukkit.broadcast(translate(broadcast))
    }

    private fun broadcastPunishment() = broadcastPunishment(punisher, type)

    @get:BsonIgnore
    val isExpired
        get() = expiration?.isBefore(Instant.now()) ?: false

}

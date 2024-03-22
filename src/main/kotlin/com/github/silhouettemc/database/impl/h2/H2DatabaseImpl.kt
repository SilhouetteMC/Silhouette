package com.github.silhouettemc.database.impl.h2

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.database.Database
import com.github.silhouettemc.history.History
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource
import com.j256.ormlite.table.TableUtils
import org.bson.conversions.Bson
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

class H2DatabaseImpl: Database {
    private val logger = LoggerFactory.getLogger(H2DatabaseImpl::class.java)

    private lateinit var conSource: JdbcPooledConnectionSource
    private lateinit var punishmentsTable: ManagedTable<Punishment, UUID>
    private lateinit var historyTable: ManagedTable<History, UUID>

    override fun initialize(plugin: Silhouette) {
        try {
            conSource = JdbcPooledConnectionSource("jdbc:h2:" + File(plugin.dataFolder, "h2.db").path + "/storage")
            punishmentsTable = createTable(Punishment::class.java)
            historyTable = createTable(History::class.java)
        } catch (exception: Exception) {
            logger.error("Error while initializing the H2 database", exception)
            plugin.server.pluginManager.disablePlugin(plugin)
        }
    }

    private fun <T, K> createTable(clazz: Class<*>): ManagedTable<T, K> {
        TableUtils.createTableIfNotExists(conSource, clazz)
        return ManagedTable(DaoManager.createDao(conSource, clazz))
    }

    override fun addHistory(history: History) {
        historyTable.create(history)
    }

    override fun addPunishment(punishment: Punishment) {
        punishmentsTable.create(punishment)
    }

    override fun updatePunishment(punishment: Punishment, vararg updates: Bson) {
        punishmentsTable.update(punishment)
    }

    override fun removePunishment(punishment: Punishment) {
        punishmentsTable.delete(punishment.id)
    }

    override fun listPunishments(player: UUID): List<Punishment> {
        return punishmentsTable.dao.filter { it.player == player }
    }

    override fun getLatestActivePunishment(player: UUID, type: PunishmentType): Punishment? {
        TODO("Not yet implemented")
    }

    override fun hasActivePunishment(player: UUID, type: PunishmentType): Boolean {
        TODO("Not yet implemented")
    }
}
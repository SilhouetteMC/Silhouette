package com.github.silhouettemc.database.impl.h2

import com.j256.ormlite.dao.Dao
import org.bukkit.Bukkit
import java.sql.SQLException
import java.util.logging.Level

class ManagedTable<T, K> internal constructor(val dao: Dao<T, K>) {
    private val logger = Bukkit.getLogger()

    fun create(klass: T): Boolean {
        try {
            dao.create(klass)
            return true
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, e.toString())
            return false
        }
    }

    fun update(klass: T): Boolean {
        try {
            dao.update(klass)
            return true
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, e.toString())
            return false
        }
    }

    fun get(id: K): T? {
        try {
            return dao.queryForId(id)
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, e.toString())
            return null
        }
    }

    fun delete(id: K): Boolean {
        try {
            dao.deleteById(id)
            return true
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, e.toString())
            return false
        }
    }
}
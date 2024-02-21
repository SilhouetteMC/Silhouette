package com.github.silhouettemc.plugin

import com.github.silhouettemc.plugin.database.DatabaseManager
import org.bukkit.plugin.java.JavaPlugin

class Silhouette : JavaPlugin() {
    private lateinit var databaseManager: DatabaseManager

    override fun onEnable() {
        databaseManager = DatabaseManager()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object {
        fun getInstance(): Silhouette {
            return getPlugin(Silhouette::class.java)
        }
    }
}

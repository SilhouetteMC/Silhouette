package com.github.uwubans.plugin

import com.github.uwubans.plugin.database.DatabaseManager
import org.bukkit.plugin.java.JavaPlugin

class UwUBans : JavaPlugin() {
    private lateinit var databaseManager: DatabaseManager

    override fun onEnable() {
        databaseManager = DatabaseManager()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object {
        fun getInstance(): UwUBans {
            return getPlugin(UwUBans::class.java)
        }
    }
}

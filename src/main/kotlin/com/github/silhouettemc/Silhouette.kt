package com.github.silhouettemc

import com.github.silhouettemc.database.Database
import com.github.silhouettemc.database.impl.h2.H2DatabaseImpl
import org.bukkit.plugin.java.JavaPlugin

class Silhouette : JavaPlugin() {
    private lateinit var database: Database

    override fun onEnable() {
        database = H2DatabaseImpl() // TODO: configuration when we have multiple databases
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

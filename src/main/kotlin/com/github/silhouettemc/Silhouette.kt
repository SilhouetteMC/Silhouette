package com.github.silhouettemc

import co.aikar.commands.PaperCommandManager
import com.github.silhouettemc.command.ClearChatCommand
import com.github.silhouettemc.command.punish.KickCommand
import com.github.silhouettemc.database.Database
import com.github.silhouettemc.database.impl.h2.H2DatabaseImpl
import com.github.silhouettemc.util.registerBaseCommands
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin

class Silhouette : JavaPlugin() {
    lateinit var database: Database
    lateinit var mm: MiniMessage

    override fun onEnable() {
        database = H2DatabaseImpl() // TODO: configuration when we have multiple databases
//        database.initialize(this)

        mm = MiniMessage.miniMessage()

        registerCommands()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun registerCommands() {
        val commandManager = PaperCommandManager(this)
        commandManager.registerBaseCommands(
            ClearChatCommand,
            KickCommand
        )
    }

    companion object {
        fun getInstance(): Silhouette {
            return getPlugin(Silhouette::class.java)
        }

        val mm: MiniMessage
            get() = getInstance().mm

    }
}

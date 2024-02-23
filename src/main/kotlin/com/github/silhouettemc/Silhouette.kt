package com.github.silhouettemc

import co.aikar.commands.PaperCommandManager
import com.github.silhouettemc.command.ClearChatCommand
import com.github.silhouettemc.command.MuteChatCommand
import com.github.silhouettemc.command.punish.KickCommand
import com.github.silhouettemc.database.Database
import com.github.silhouettemc.database.impl.h2.H2DatabaseImpl
import com.github.silhouettemc.listener.player.PlayerChatListener
import com.github.silhouettemc.util.registerBaseCommands
import com.github.silhouettemc.util.registerEvents
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
        registerListeners()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun registerCommands() {
        val commandManager = PaperCommandManager(this)
        commandManager.registerBaseCommands(
            ClearChatCommand,
            MuteChatCommand,
            KickCommand
        )
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(this,
            PlayerChatListener
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

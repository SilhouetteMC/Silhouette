package com.github.silhouettemc

import co.aikar.commands.ACFUtil
import co.aikar.commands.PaperCommandManager
import com.github.silhouettemc.command.ClearChatCommand
import com.github.silhouettemc.command.MuteChatCommand
import com.github.silhouettemc.command.punish.BanCommand
import com.github.silhouettemc.command.punish.KickCommand
import com.github.silhouettemc.database.Database
import com.github.silhouettemc.database.impl.mongo.MongoDatabaseImpl
import com.github.silhouettemc.listener.player.PlayerChatListener
import com.github.silhouettemc.listener.player.PlayerLoginListener
import com.github.silhouettemc.util.type.CustomMiniMessage
import com.github.silhouettemc.util.registerBaseCommands
import com.github.silhouettemc.util.registerEvents
import com.github.silhouettemc.util.type.ReasonContext
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.plugin.java.JavaPlugin


class Silhouette : JavaPlugin() {
    lateinit var database: Database
    lateinit var mm: MiniMessage

    override fun onEnable() {
        database = MongoDatabaseImpl() // TODO: configuration when we have multiple databases
        database.initialize(this)

        mm = CustomMiniMessage().build()

        registerCommands()
        registerListeners()
    }

    override fun onDisable() {
        // Plugin shutdown logic

//        todo: disconnect from database on disable, ie:
//        database.disconnect()
    }

    private fun registerCommands() {
        val commandManager = PaperCommandManager(this)
        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerContexts()
        commandManager.registerBaseCommands(
            BanCommand, KickCommand,
            ClearChatCommand, MuteChatCommand
        )
    }

    private fun PaperCommandManager.registerContexts() {
        // soon:tm:
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(this,
            PlayerChatListener,
            PlayerLoginListener,
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

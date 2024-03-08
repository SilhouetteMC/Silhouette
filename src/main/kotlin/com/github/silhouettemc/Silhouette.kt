package com.github.silhouettemc

import co.aikar.commands.PaperCommandManager
import com.github.silhouettemc.command.ClearChatCommand
import com.github.silhouettemc.command.MuteChatCommand
import com.github.silhouettemc.command.punish.KickCommand
import com.github.silhouettemc.database.Database
import com.github.silhouettemc.database.impl.mongo.MongoDatabaseImpl
import com.github.silhouettemc.listener.player.PlayerChatListener
import com.github.silhouettemc.util.CustomMiniMessage
import com.github.silhouettemc.util.registerBaseCommands
import com.github.silhouettemc.util.registerEvents
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
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

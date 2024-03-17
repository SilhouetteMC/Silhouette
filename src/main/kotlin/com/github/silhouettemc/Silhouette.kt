package com.github.silhouettemc

import co.aikar.commands.PaperCommandManager
import com.github.silhouettemc.command.chat.ClearChatCommand
import com.github.silhouettemc.command.chat.MuteChatCommand
import com.github.silhouettemc.command.punish.BanCommand
import com.github.silhouettemc.command.punish.KickCommand
import com.github.silhouettemc.command.punish.MuteCommand
import com.github.silhouettemc.database.Database
import com.github.silhouettemc.database.impl.mongo.MongoDatabaseImpl
import com.github.silhouettemc.listener.player.PlayerChatListener
import com.github.silhouettemc.listener.player.PlayerLoginListener
import com.github.silhouettemc.util.text.CustomMiniMessage
import com.github.silhouettemc.util.registerBaseCommands
import com.github.silhouettemc.util.registerEvents
import com.github.silhouettemc.util.parsing.PlayerProfileRetriever
import com.github.silhouettemc.util.parsing.PunishArgumentTabCompleter
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
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
        commandManager.registerCommandCompletions()
        commandManager.registerBaseCommands(
            BanCommand, KickCommand, MuteCommand,
            ClearChatCommand, MuteChatCommand
        )
    }

    private fun PaperCommandManager.registerContexts() {
        this.getCommandContexts().registerContext(PlayerProfileRetriever::class.java) { context ->
            PlayerProfileRetriever(context.popFirstArg())
        }
    }

    private fun PaperCommandManager.registerCommandCompletions() {
        this.commandCompletions.setDefaultCompletion("players", OfflinePlayer::class.java, PlayerProfileRetriever::class.java)
        this.commandCompletions.registerCompletion("punish_args") { context -> PunishArgumentTabCompleter.getCompletions(context) }
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

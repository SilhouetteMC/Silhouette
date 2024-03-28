package com.github.silhouettemc

import BstatsBuilder
import co.aikar.commands.PaperCommandManager
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.silhouettemc.command.chat.ClearChatCommand
import com.github.silhouettemc.command.chat.MuteChatCommand
import com.github.silhouettemc.command.plugin.reload.ReloadCommand
import com.github.silhouettemc.command.punish.BanCommand
import com.github.silhouettemc.command.punish.KickCommand
import com.github.silhouettemc.command.punish.MuteCommand
import com.github.silhouettemc.command.punish.revert.UnbanCommand
import com.github.silhouettemc.command.punish.revert.UnmuteCommand
import com.github.silhouettemc.database.Database
import com.github.silhouettemc.database.impl.h2.H2DatabaseImpl
import com.github.silhouettemc.database.impl.mongo.MongoDatabaseImpl
import com.github.silhouettemc.listener.player.PlayerChatListener
import com.github.silhouettemc.listener.player.PlayerLoginListener
import com.github.silhouettemc.listener.player.PlayerTabCompleteListener
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.parsing.PlayerProfileRetriever
import com.github.silhouettemc.parsing.punish.PunishArgumentTabCompleter
import com.github.silhouettemc.util.registerBaseCommands
import com.github.silhouettemc.util.registerEvents
import com.github.silhouettemc.util.text.SilhouetteMiniMessage
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.OfflinePlayer

class Silhouette : SuspendingJavaPlugin() {
    lateinit var database: Database
    lateinit var mm: MiniMessage

    override suspend fun onEnableAsync() {
        ConfigUtil.load()
        BstatsBuilder.build(this)

        when(val dbType = ConfigUtil.config.getString("database.type")) {
           "mongo" -> database = MongoDatabaseImpl(this)
           "h2" -> database = H2DatabaseImpl()
           else ->  this.logger.warning("The database type of $dbType is something we don't support for Silhouette! Accepted types are: mongo, h2.")
        }

        database.initialize(this)

        mm = SilhouetteMiniMessage().build()

        registerCommands()
        registerListeners()
    }

    override suspend fun onDisableAsync() {
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
            UnbanCommand, UnmuteCommand,
            ClearChatCommand, MuteChatCommand,
            ReloadCommand
        )
    }

    private fun PaperCommandManager.registerContexts() {
        this.getCommandContexts().registerContext(PlayerProfileRetriever::class.java) { context ->
            PlayerProfileRetriever(context.popFirstArg())
        }
    }

    private fun PaperCommandManager.registerCommandCompletions() {
        this.commandCompletions.setDefaultCompletion("players", OfflinePlayer::class.java, PlayerProfileRetriever::class.java)
        this.commandCompletions.registerCompletion("punish_args") { context -> PunishArgumentTabCompleter.getDurationAndFlagCompletions(context) }
        this.commandCompletions.registerCompletion("punish_flags") { context -> PunishArgumentTabCompleter.getFlagCompletions(context) }
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(this,
            PlayerChatListener(this),
            PlayerLoginListener(this),
            PlayerTabCompleteListener()
        )
    }

    companion object {
        fun getInstance(): Silhouette {
            return getPlugin(Silhouette::class.java)
        }

        var mm: MiniMessage
            get() = getInstance().mm
            set(value) {
                getInstance().mm = value
            }
    }
}

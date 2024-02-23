package com.github.silhouettemc.util

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

/**
 * Register all [BaseCommand] for a [PaperCommandManager].
 * @param [commands] to build then register.
 */
fun PaperCommandManager.registerBaseCommands(vararg commands: BaseCommand) {
    for(cmd in commands) {
        this.registerCommand(cmd)
    }
}

/**
 * Register all [Listener] for a [PluginManager].
 * @param [plugin] to register the listeners for.
 * @param [listeners] to register.
 */
fun PluginManager.registerEvents(plugin: JavaPlugin, vararg listeners: Listener) {
    for(listener in listeners) {
        registerEvents(listener, plugin)
    }
}
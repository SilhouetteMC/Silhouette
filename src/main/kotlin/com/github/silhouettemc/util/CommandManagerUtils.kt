package com.github.silhouettemc.util

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import org.bukkit.command.CommandSender

/**
 * Register all [BaseCommand] for a [PaperCommandManager].
 * @param [commands] to build then register.
 */
fun PaperCommandManager.registerBaseCommands(vararg commands: BaseCommand) {
    for(cmd in commands) {
        this.registerCommand(cmd)
    }
}
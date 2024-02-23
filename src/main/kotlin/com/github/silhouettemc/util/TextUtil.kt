package com.github.silhouettemc.util

import com.github.silhouettemc.Silhouette.Companion.mm
import org.bukkit.command.CommandSender

fun translate(input: String) = mm.deserialize(input)
fun warning(input: String) = translate("<#ff6e6e>⚠ <#ff7f6e>$input") // todo: configurable error format

fun CommandSender.sendError(error: String) {
    this.sendMessage(warning(error))
}
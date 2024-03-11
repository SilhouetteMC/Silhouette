package com.github.silhouettemc.util

import com.github.silhouettemc.Silhouette.Companion.mm
import org.bukkit.command.CommandSender
import java.util.*

fun translate(input: String) = mm.deserialize(input)
fun warning(input: String) = translate("<#ff6e6e>⚠ <#ff7f6e>$input") // todo: configurable error format

fun CommandSender.sendError(error: String) {
    this.sendMessage(warning(error))
}

fun CommandSender.sendTranslated(message: String) {
    this.sendMessage(translate(message))
}

fun UUID.withoutDashes() = this.toString().replace("-", "")
fun String.toUUID(): UUID = UUID.fromString(this.insertUUIDDashes())

/**
 * Inserts dashes into a UUID string, or returns the string if it already has dashes (checks if it has 36 characters compared to 32)
 */
fun String.insertUUIDDashes(): String {
    if (length == 36) return this
    if (length != 32) throw IllegalArgumentException("Invalid UUID length")
    return StringBuilder()
        .append(substring(0, 8))
        .append("-")
        .append(substring(8, 12))
        .append("-")
        .append(substring(12, 16))
        .append("-")
        .append(substring(16, 20))
        .append("-")
        .append(substring(20))
        .toString()
}
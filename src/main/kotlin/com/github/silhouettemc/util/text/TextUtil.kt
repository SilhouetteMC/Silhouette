package com.github.silhouettemc.util.text

import com.github.silhouettemc.Silhouette.Companion.mm
import com.github.silhouettemc.util.ConfigUtil
import org.bukkit.command.CommandSender
import java.util.*

fun translate(input: String) = mm.deserialize(input)

fun CommandSender.sendError(error: String) = this.send(error)
fun CommandSender.sendTranslated(message: String) = this.sendMessage(translate(message))

fun CommandSender.send(key: String, placeholders: Map<String, String>? = null)
    = this.sendTranslated(ConfigUtil.getMessage(key, placeholders))

fun CommandSender.sendBar(key: String, placeholders: Map<String, String>? = null)
    = this.sendActionBar(translate(ConfigUtil.getActionBar(key, placeholders)))

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

fun String.replacePlaceholders(map: Map<String, String>, parenthesis: String = "{}", ignoreCase: Boolean = false) : String {
    val totalPlaceholders = mutableMapOf<String, String>()
    val customPlaceholders = ConfigUtil.messages.getTable("custom_placeholders").toMap()

    customPlaceholders.forEach {
        totalPlaceholders[it.key] = it.value.toString()
    }
    totalPlaceholders.putAll(map)

    var placeholded = this
    for (value in totalPlaceholders) {
        placeholded = placeholded.replace("${parenthesis[0]}${value.key}${parenthesis[1]}", value.value, ignoreCase)
    }
    return placeholded
}
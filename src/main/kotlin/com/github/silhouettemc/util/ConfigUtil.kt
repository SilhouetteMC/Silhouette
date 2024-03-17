package com.github.silhouettemc.util

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.util.text.CustomMiniMessage
import com.github.silhouettemc.util.text.replacePlaceholders
import com.moandjiezana.toml.Toml
import org.bukkit.Bukkit
import java.nio.file.Files

object ConfigUtil {
    lateinit var config: Toml
    lateinit var messages: Toml

    private val configs = listOf(
        "config.toml",
        "messages.toml",
    )

    fun load() {
        val plugin = Silhouette.getInstance()
        plugin.dataFolder.mkdirs()

        configs.forEach { file ->
            val configPath = plugin.dataFolder.resolve(file)
            if(!configPath.exists()) plugin.saveResource(file, false)
            val data = String(Files.readAllBytes(configPath.toPath()))

            try {
                when (file) {
                    "config.toml" -> config = Toml().read(data)
                    "messages.toml" -> messages = Toml().read(data)
                }
            } catch (ex: Exception) {
                plugin.logger.severe("Failed to load configuration! ${ex.message}")
                Bukkit.getPluginManager().disablePlugin(plugin)
            }
        }

        // Rebuilds MiniMessage to update it
        Silhouette.mm = CustomMiniMessage().build()
    }

    fun getMessage(key: String): String {
        val msg = messages.getString("messages.$key") ?: return key
        return msg.trimIndent()
    }

    fun getMessage(key: String, placeholders: Map<String, String>): String {
        val msg = messages.getString("messages.$key") ?: return key
        return msg.trimIndent().replacePlaceholders(placeholders)
    }
}
package com.github.silhouettemc.util

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.util.text.SilhouetteMiniMessage
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
        Silhouette.mm = SilhouetteMiniMessage().build()
    }

    fun getMessage(key: String, placeholders: Map<String, String>? = null)
        = retrieveString(messages, "messages.$key", placeholders)

    fun getActionBar(key: String, placeholders: Map<String, String>? = null, allowExtraPlaceholders: List<String>? = null)
        = retrieveString(messages, "actionbars.$key", placeholders, allowExtraPlaceholders)

    private fun retrieveString(config: Toml, key: String, placeholders: Map<String, String>? = null, allowExtraPlaceholders: List<String>? = null): String {
        val baseKey = key.split(".").dropLast(1).joinToString(".")

        val extraPlaceholders = mutableMapOf<String, String>()

        for (pKey in messages.getTable(baseKey).entrySet().map { it.key }) {
            if (!pKey.startsWith("p_")) continue
            if (allowExtraPlaceholders != null && !allowExtraPlaceholders.contains(pKey.removePrefix("p_"))) extraPlaceholders[pKey] = ""
            else extraPlaceholders[pKey] = config.getString("$baseKey.$pKey")
        }

        val configString = config.getString(key) ?: return key
        val replacedExtras = configString.replacePlaceholders(extraPlaceholders)

        return if (placeholders != null) replacedExtras.replacePlaceholders(placeholders)
        else replacedExtras
    }

}
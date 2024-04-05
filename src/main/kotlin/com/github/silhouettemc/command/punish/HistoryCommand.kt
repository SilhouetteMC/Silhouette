package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.parsing.PlayerProfileRetriever
import com.github.silhouettemc.parsing.duration.TimeFormatter
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.gui.fillGlass
import com.github.silhouettemc.util.gui.setDescription
import com.github.silhouettemc.util.gui.setName
import com.github.silhouettemc.util.sync
import com.github.silhouettemc.util.text.send
import com.github.silhouettemc.util.text.toLegacy
import org.bukkit.entity.Player
import java.util.function.BiConsumer
import me.honkling.pocket.GUI
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.time.Duration
import java.time.Instant

@CommandAlias("history")
@Description("View the punishment history of a player")
@CommandPermission("silhouette.punish.history")
object HistoryCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: Silhouette

    private fun GUI.loadItems(data: List<Punishment>, itemCallback: BiConsumer<Punishment, Int>) {
        val items = this.template.replace("\n", "")

        var index = 0
        var itemIndex = 0

        for (row in items.lines()) {
            for (char in row) {
                if (char == 'i'  && itemIndex < data.size) {
                    itemCallback.accept(data[itemIndex], index)
                    itemIndex++
                }  else if(char == 'i') {
                    val item = ItemStack(Material.PINK_STAINED_GLASS_PANE).setName("<red>")
                    this.put(index, item)
                }
                index++
            }
        }
    }

    private fun prettyDate(date: Instant): String {
        val now = Instant.now()
        val isInPast = date.isBefore(now)

        val duration = if (isInPast)
            Duration.between(date, now)
        else
            Duration.between(now, date)

        var time = TimeFormatter(duration).prettify(round = true)

        if(isInPast) time += " ago"
        else time = "in $time"

        return time
    }


    @Default
    fun onCommand(
        sender: Player,
        @Flags("other") retriever: PlayerProfileRetriever,
        @Optional type: String?,
        @Optional pageNumber: Int?,
    ) = plugin.launch(plugin.asyncDispatcher) {
        val page = pageNumber ?: 1

        val basicPlaceholders = mutableMapOf(
            "target" to retriever.name,
            "page" to page.toString(),
        )

        if(type != null && type != "all") {
            val foundType = PunishmentType.entries.find { it.name.equals(type.uppercase(), true) }
            if (foundType == null) return@launch sender.send("errors.noType", basicPlaceholders)
        }

        val pageSize = 18

        if (page < 1) return@launch sender.send("errors.noHistory", basicPlaceholders)

        val player = retriever.fetchOfflinePlayerProfile()
            ?: return@launch sender.send("errors.noPlayerFound", basicPlaceholders)
        val playerName = player.name
            ?: return@launch sender.send("errors.noPlayerFound", basicPlaceholders)

        val fullHistory = if(type != null && type != "all") {
            Silhouette.getInstance().database.listPunishments(player.id!!, PunishmentType.valueOf(type.uppercase()))
        } else {
            Silhouette.getInstance().database.listPunishments(player.id!!)
        }

        if (fullHistory.isEmpty()) return@launch sender.send("errors.noHistory", basicPlaceholders)

        val historySize = fullHistory.size
        val totalPages = historySize / pageSize
        if (page < totalPages) return@launch sender.send("errors.noHistory", basicPlaceholders)
        val start = (page - 1) * pageSize
        if (start > historySize) return@launch sender.send("errors.noHistory", basicPlaceholders)
        var end = start + pageSize
        if (end > historySize) end = historySize
        val history = fullHistory.subList(start, end)

        basicPlaceholders["target"] = playerName
        basicPlaceholders["page"] = page.toString()
        basicPlaceholders["totalPages"] = totalPages.toString()

        val guiTitle = ConfigUtil.getMessage("gui.history.title", basicPlaceholders)

        val gui = GUI(
            plugin,
            """
            xxxxxxxxx
            xiiiiiiix
            xiiiiiiix
            xiiiiiiix
            xxxxxxxxx
            """.trimIndent(),
            guiTitle.toLegacy(),
        ).fillGlass()

        gui.loadItems(history) { punishment, index ->
            val reason = punishment.reason ?: "No reason specified"
            val expiry = if(punishment.expiration == null) "Never" else {
                prettyDate(punishment.expiration)
            }
            val happenedAt = prettyDate(punishment.punishedOn.toInstant())

            val placeholders = mutableMapOf(
                "reason" to reason,
                "happenedAt" to happenedAt,
                "punisher" to punishment.punisher.getReadableName(),
                "type" to punishment.type.actionName.lowercase(),
                "expiry_tag" to "Expires",
                "expiry_date" to expiry
            )

            placeholders.putAll(basicPlaceholders)

            if(expiry.contains("Never")) placeholders["expiry_date"] = "Never"
            else if(expiry.contains("ago")) placeholders["expiry_tag"] = "Expired"

            if(punishment.flags != null && punishment.flags!!.isNotEmpty()) {
                val flags = punishment.flags!!.joinToString("<p>, <s>") { it.name.lowercase() }
                placeholders["flags"] = flags
            } else placeholders["flags"] = "None"

            val historyLore = ConfigUtil.getMessage("gui.history.itemLore", placeholders)

            val item = ItemStack(Material.PAPER)
            item.setName(ConfigUtil.getMessage("gui.history.itemName", placeholders))
            item.setDescription(
                *historyLore.trimIndent().split("\n").toTypedArray()
            )

            gui.put(index, item) {
                if(it.isRightClick && it.isShiftClick) {
                    sender.sendMessage("right click shift")
                } else if(it.isLeftClick) {
                    sender.sendMessage("left click")
                }
            }
        }

        gui.put(36, ItemStack(Material.ARROW).setName("Previous Page")) {
            val punishmentType = type ?: "all"
            sender.performCommand("history $playerName $punishmentType ${page - 1}")
        }
        gui.put(44, ItemStack(Material.ARROW).setName("Next Page")) {
            val punishmentType = type ?: "all"
            sender.performCommand("history $playerName $punishmentType ${page + 1}")
        }
        sync {
            gui.open(sender)
        }
    }
}
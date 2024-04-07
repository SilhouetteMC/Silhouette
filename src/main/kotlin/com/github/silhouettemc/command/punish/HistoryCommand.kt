package com.github.silhouettemc.command.punish

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.parsing.PlayerProfileRetriever
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.gui.Anvil
import com.github.silhouettemc.util.gui.fillGlass
import com.github.silhouettemc.util.gui.setLoreFromConfig
import com.github.silhouettemc.util.gui.setName
import com.github.silhouettemc.util.sync
import com.github.silhouettemc.util.text.send
import com.github.silhouettemc.util.text.titleCase
import com.github.silhouettemc.util.text.toLegacy
import me.honkling.pocket.GUI
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.UUID
import java.util.function.BiConsumer


private data class HistoryData(
    val punishments: List<Punishment>,
    val punishType: PunishmentType,
    val page: Int,
    val totalPages: Int,
    val target: String,
    val basicPlaceholders: Map<String, String>
)

private val historyToggles = mutableMapOf<UUID, List<Int>>()

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

    private fun formatDate(date: Instant): String {
        val dateFormat = ConfigUtil.config.getString("dateFormat") ?: "MM/dd/yyyy HH:mm:ss"
        val formatter = SimpleDateFormat(dateFormat)
        val formattedDate = formatter.format(Date.from(date))
        return formattedDate
    }

    private fun generateTypeBook(currentType: PunishmentType): ItemStack {
        val name = currentType.name.lowercase()

        val punishmentTypes = PunishmentType.entries.map { it.name.lowercase() }
            .filterNot { it.contains("un", ignoreCase = true) }

        // hacky, but if a type is selected, the bullet point is replaced with an arrow to indicate it's selected
        val typeSelector = StringBuilder()

        punishmentTypes.forEach {
            val placeholders = mapOf(
                "item" to it.titleCase(),
                "description" to "View $it punishments"
            )

            val item = if(it == name) {
                ConfigUtil.getMessage("gui.history.selectedHistoryType",placeholders)
            } else {
                ConfigUtil.getMessage("gui.history.historyType", placeholders)
            }

            typeSelector.append("$item\n")
        }

        val typeLore = ConfigUtil.getMessage("gui.history.typeLore", mapOf(
            "type" to name,
            "type_selector" to typeSelector.toString()
        ))

        val typeBook = ItemStack(Material.ENCHANTED_BOOK).setName("Type")
            .setLoreFromConfig(typeLore)
        return typeBook
    }

    private fun getNextType(type: PunishmentType, down: Boolean = false): PunishmentType {
        val index = PunishmentType.entries.indexOf(type)

        val newIndex = if(down) index + 1 else index - 1
        var item = if(PunishmentType.entries.size == newIndex) PunishmentType.ALL
            else PunishmentType.entries[newIndex]

        // these are types that aren't in the gui (& are at the end), so we need to change them to the first type in the gui
        if(item.actionName.contains("un", ignoreCase = true)) item = PunishmentType.ALL

        return item
    }

    private fun Player.getGUI(data: HistoryData): GUI {
        val sender = this

        val guiTitle = ConfigUtil.getMessage("gui.history.title", data.basicPlaceholders)

        val gui = GUI(
            plugin,
            """
            xxxxxxxxx
            xiiiiiiix
            xiiiiiiix
            xiiiiiiix
            xxxxtxxxx
            """.trimIndent(),
            guiTitle.toLegacy(),
        ).fillGlass()

        gui.loadItems(data.punishments) { punishment, index ->
            val reason = punishment.reason ?: "No reason specified"
            val expiry = if(punishment.expiration == null) "Never" else {
                formatDate(punishment.expiration)
            }
            val happenedAt = formatDate(punishment.punishedOn.toInstant())

            val placeholders = mutableMapOf(
                "reason" to reason,
                "happenedAt" to happenedAt,
                "punisher" to punishment.punisher.getReadableName(),
                "type" to punishment.type.actionName.lowercase(),
                "type_2" to punishment.type.punishedName.titleCase(),
                "expiry_tag" to "Expires",
                "expiry_date" to expiry,
                "id" to punishment.id.toString()
            )

            placeholders.putAll(data.basicPlaceholders)

            if(expiry.contains("Never")) placeholders["expiry_date"] = "Never"
            else if(punishment.expiration != null && punishment.expiration.isBefore(Instant.now())) placeholders["expiry_tag"] = "Expired"

            val isReverted = punishment.revoker != null
            var itemLore = ConfigUtil.getMessage("gui.history.lore", placeholders)

            if(isReverted) {
                placeholders.putAll(mapOf(
                    "reverter" to punishment.revoker!!.getReadableName(),
                    "reversion_date" to formatDate(punishment.revokedAt!!.toInstant()),
                    "reversion_reason" to (punishment.revokeReason ?: "No reason specified")
                ))
                itemLore = ConfigUtil.getMessage("gui.history.revertedLore", placeholders)
            }

            val item = ItemStack(Material.PAPER)
            item.setName(ConfigUtil.getMessage("gui.history.name", placeholders))
            item.setLoreFromConfig(itemLore)

            if(placeholders["expiry_tag"] == "Expires" && !isReverted) {
                val meta = item.itemMeta
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true)
                item.itemMeta = meta
            }

            fun callback(ev: InventoryClickEvent) {
                if(isReverted) return

                val isEditing = ev.isLeftClick
                val isReverting = ev.isShiftClick && ev.isRightClick
                val isViewingHistory = ev.click == ClickType.MIDDLE

                if(isViewingHistory) {
                    val currentToggles = historyToggles[sender.uniqueId] ?: listOf()

                    sender.sendMessage("Current history toggles: ${currentToggles.joinToString(", ")}")

                    val isToggled = currentToggles.contains(index)
                    val newToggles = if(isToggled) currentToggles.filterNot { i -> i == index } else currentToggles + index
                    historyToggles[sender.uniqueId] = newToggles

                    sender.sendMessage("New history toggles: ${newToggles.joinToString(", ")}")

                    plugin.launch(plugin.asyncDispatcher) {
                        if(!isToggled) {
                            itemLore = ConfigUtil.getMessage("gui.history.historyLore", mapOf(
                                "history" to generateHistory(punishment)
                            ))
                            item.setLoreFromConfig(itemLore)
                            gui.put(index, item) {
                                callback(it)
                            }
                        } else {
                            itemLore = ConfigUtil.getMessage("gui.history.lore", placeholders)
                            item.setLoreFromConfig(itemLore)
                            gui.put(index, item) {
                                callback(it)
                            }
                        }
                    }

                    gui.open(sender)

                    return
                }

                if(!isEditing && !isReverting) return

                val editTitle = ConfigUtil.getMessage("gui.editPunishmentReason.title")

                if(isEditing) {
                    val editAnvil = Anvil("editPunishment", editTitle)
                    editAnvil.open(sender, itemName = punishment.reason) { newReason ->
                        plugin.launch(plugin.asyncDispatcher) {
                            punishment.updateReason(newReason)
                        }
                        sender.send("gui.editPunishmentReason.successfulEdit", mapOf("reason" to newReason))
                    }
                    return
                }

                val revertAnvil = Anvil("revertPunishment", editTitle)
                revertAnvil.open(sender) { newReason ->
                    plugin.launch(plugin.asyncDispatcher) {
                        punishment.revert(Actor(sender.uniqueId), newReason)
                    }
                    sender.send("gui.editPunishmentReason.successfulRevert", mapOf("reason" to newReason))
                }
                return
            }

            gui.put(index, item) {
                callback(it)
            }
        }

        gui.put('t', generateTypeBook(data.punishType)) {
            val nextType = getNextType(data.punishType, down = it.isLeftClick)
            sender.performCommand("history ${data.target} $nextType")
        }

        gui.put(36, ItemStack(Material.ARROW).setName("Previous Page")) {
            val punishmentType = data.punishType.name.lowercase()
            sender.performCommand("history ${data.target} $punishmentType ${data.page - 1}")
        }

        gui.put(44, ItemStack(Material.ARROW).setName("Next Page")) {
            val punishmentType = data.punishType.name.lowercase()
            sender.performCommand("history ${data.target} $punishmentType ${data.page  + 1}")
        }

        return gui
    }

    private suspend fun generateHistory(punishment: Punishment): String {
        val historyStr = mutableListOf<String>()

        val history = Silhouette.getInstance().database.getHistory(punishment.id)

        if(history.isEmpty()) return "No history found"

        for (h in history) {
            if(h.reason?.current == null || h.reason.prior == null) continue

            val added = ConfigUtil.getMessage("gui.history.addedReason", mapOf("reason" to h.reason.current))
            val removed = ConfigUtil.getMessage("gui.history.removedReason", mapOf("reason" to h.reason.prior))

            historyStr.add(added)
            historyStr.add(removed)
        }

        return historyStr.joinToString("\n")
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

        lateinit var punishType: PunishmentType

        if(type != null) {
            val foundType = PunishmentType.entries.find { it.name.equals(type!!.uppercase(), true) }
            if (foundType == null) return@launch sender.send("errors.noType", basicPlaceholders)
            punishType = foundType
        } else punishType = PunishmentType.ALL

        val pageSize = 18

        if (page < 1) return@launch sender.send("errors.noHistory", basicPlaceholders)

        val player = retriever.fetchOfflinePlayerProfile()
            ?: return@launch sender.send("errors.noPlayerFound", basicPlaceholders)
        val playerName = player.name
            ?: return@launch sender.send("errors.noPlayerFound", basicPlaceholders)

        val fullHistory = if(punishType != PunishmentType.ALL) {
            plugin.database.listPunishments(player.id!!, punishType)
        } else {
            plugin.database.listPunishments(player.id!!)
        }

        if (fullHistory.isEmpty()) {
            val gui = sender.getGUI(HistoryData(listOf(), punishType, 1, 1, playerName, basicPlaceholders))
            val item = ItemStack(Material.PAPER).setName("No history found")
            gui.put(22, item)
            sync {
                gui.open(sender)
            }
            return@launch
        }

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

        val gui = sender.getGUI(HistoryData(history, punishType, page, totalPages, playerName, basicPlaceholders))

        sync {
            gui.open(sender)
        }
    }
}
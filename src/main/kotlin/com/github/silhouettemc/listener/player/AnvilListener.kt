package com.github.silhouettemc.listener.player

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.util.gui.EditPunishmentGUI
import com.github.silhouettemc.util.text.send
import com.mongodb.client.model.Updates
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.ItemStack

class AnvilListener(
    private val plugin: Silhouette
) : Listener {
    @EventHandler
    fun PrepareAnvilEvent.onRename() {
        if (view.title != EditPunishmentGUI.TITLE) return
        val player = view.player as Player

        val nameComponent = inventory.result?.itemMeta?.displayName() ?: return

        val item = ItemStack(Material.BOOK)
        val meta = item.itemMeta

        meta.displayName(nameComponent)
        item.itemMeta = meta

        inventory.result = item
    }

    @EventHandler
    fun InventoryClickEvent.onClick() {
        if (inventory.type != InventoryType.ANVIL || view.title != EditPunishmentGUI.TITLE) return

        isCancelled = true
        if (slot != 2) return

        val player = view.player as Player

        val rawReason = currentItem?.itemMeta?.displayName() ?: return
        val serializer = PlainTextComponentSerializer.plainText()
        val newReason = serializer.serialize(rawReason)

        val punishment = EditPunishmentGUI.editors[player.uniqueId] ?: return

        plugin.launch(plugin.asyncDispatcher) {
            plugin.database.updatePunishment(punishment,
                Updates.set(Punishment::reason.name, newReason)
            )
        }

        player.send("gui.editPunishmentReason.successfulEdit", mapOf("reason" to newReason))
        inventory.close()
    }

    @EventHandler
    fun InventoryCloseEvent.onClose() {
        if (view.title != EditPunishmentGUI.TITLE) return
        inventory.clear()
        EditPunishmentGUI.editors.remove(player.uniqueId)
    }
}
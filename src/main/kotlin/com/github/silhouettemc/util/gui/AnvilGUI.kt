package com.github.silhouettemc.util.gui

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.util.sync
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack
import java.util.UUID

typealias AnvilHandler = (String) -> Any?

private val onClickEvents = mutableMapOf<String, AnvilHandler>()
private val serializer = PlainTextComponentSerializer.plainText()
val openAnvils = mutableMapOf<UUID, Anvil>()

class Anvil(val id: String, private val title: String? = null) {
    fun open(executor: Player, itemName: String? = null, onType: AnvilHandler = {}) {
        sync {
            openAnvils[executor.uniqueId] = this

            val view = executor.openAnvil(null, true) ?: return@sync
            if (title != null) view.title = title
            view.setItem(0,  ItemStack(Material.ANVIL).setName(itemName ?: "Enter text"))

            val inv = view.topInventory as AnvilInventory

            registerListeners(inv)

            onClickEvents[this.id] = onType
        }

    }

    private fun registerListeners(inv: AnvilInventory) {
        val pluginManager = Bukkit.getPluginManager()

        pluginManager.registerEvents(object : Listener {
            val listener = this

            @EventHandler
            fun PrepareAnvilEvent.onRename() {
                val nameComponent = inventory.result?.itemMeta?.displayName() ?: return

                val item = ItemStack(Material.BOOK)
                val meta = item.itemMeta

                meta.displayName(nameComponent)
                item.itemMeta = meta

                inventory.result = item
            }

            @EventHandler
            fun InventoryClickEvent.onClick() {
                if (inventory.type != InventoryType.ANVIL) return

                isCancelled = true
                if (slot != 2) return

                val text = inv.result?.itemMeta?.displayName() ?: return

                onClickEvents[id]!!.invoke(serializer.serialize(text))

                inventory.close()
            }

            @EventHandler
            fun InventoryCloseEvent.onClose() {
                if (inventory.type != InventoryType.ANVIL) return

                onClickEvents.remove(id)

                InventoryClickEvent.getHandlerList().unregister(listener)
                InventoryCloseEvent.getHandlerList().unregister(listener)
            }
        }, Silhouette.getInstance())
    }
}
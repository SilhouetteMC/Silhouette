package com.github.silhouettemc.util.gui

import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.text.toLegacy
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.UUID

object EditPunishmentGUI {
    val TITLE = ConfigUtil.getMessage("gui.editPunishment.title").toLegacy()
    val editors = mutableMapOf<UUID, Punishment>()

    fun open(player: Player, punishment: Punishment) {
        if (editors.containsKey(player.uniqueId)) return
        editors[player.uniqueId] = punishment

        val view = player.openAnvil(null, true) ?: return

        view.title = TITLE
        view.setItem(0,  ItemStack(Material.ANVIL).setName(punishment.reason ?: "No reason specified"))
    }
}
package com.github.silhouettemc.util.gui

import me.honkling.pocket.GUI
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Fills a GUI with glass
 *
 *  @property material the material to fill the glass with
 *  @return the GUI with the glass filled
 * */
fun GUI.fillGlass(material: Material? = null): GUI {
    val mat = material ?: Material.PINK_STAINED_GLASS_PANE
    this.put('x', ItemStack(mat).setName("<red>"))
    return this
}
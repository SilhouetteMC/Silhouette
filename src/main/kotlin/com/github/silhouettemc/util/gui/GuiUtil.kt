package com.github.silhouettemc.util.gui

import me.honkling.pocket.GUI
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun GUI.fillGlass(material: Material? = null): GUI {
    val mat = material ?: Material.PINK_STAINED_GLASS_PANE
    this.put('x', ItemStack(mat).setName("<red>"))
    return this
}
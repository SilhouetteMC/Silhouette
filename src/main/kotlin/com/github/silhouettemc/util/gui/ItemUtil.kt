package com.github.silhouettemc.util.gui

import com.github.silhouettemc.Silhouette.Companion.mm
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack

fun ItemStack.setName(name: String): ItemStack {
    val meta = this.itemMeta
    name.let {
        val coloredName = mm.deserialize(it).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        meta.displayName(coloredName)
    }
    this.itemMeta = meta
    return this
}

fun ItemStack.setDescription(lore: List<String>): ItemStack {
    val meta = this.itemMeta
    meta.lore(lore.map {
        mm.deserialize(it).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    })
    this.itemMeta = meta
    return this
}

fun ItemStack.setDescription(vararg lore: String): ItemStack {
    return this.setDescription(listOf(*lore))
}
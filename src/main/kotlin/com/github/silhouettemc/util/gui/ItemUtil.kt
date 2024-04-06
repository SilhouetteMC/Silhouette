package com.github.silhouettemc.util.gui

import com.github.silhouettemc.Silhouette.Companion.mm
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack

/**
 * Sets an items name
 *
 *  @property name the name to set
 *  @return the item with the name set
 * */
fun ItemStack.setName(name: String): ItemStack {
    val meta = this.itemMeta
    name.let {
        val coloredName = mm.deserialize(it).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        meta.displayName(coloredName)
    }
    this.itemMeta = meta
    return this
}

/**
 * Sets an items lore
 *
 *  @property lore a list of lore lines
 *  @return the item with the lore set
 * */
fun ItemStack.setDescription(lore: List<String>): ItemStack {
    val meta = this.itemMeta
    meta.lore(lore.map {
        mm.deserialize(it).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    })
    this.itemMeta = meta
    return this
}

/**
 * Sets an items lore
 *
 *  @property lore a vararg of lore lines
 *  @return the item with the lore set
 * */
fun ItemStack.setDescription(vararg lore: String): ItemStack {
    return this.setDescription(listOf(*lore))
}

/**
* Sets an items lore from a config string
*
*  @property msg the parsed config string
*  @return the item with the lore set
* */
fun ItemStack.setLoreFromConfig(msg: String): ItemStack {
    val lines = msg.trimIndent().split("\n")
    return this.setDescription(lines)
}
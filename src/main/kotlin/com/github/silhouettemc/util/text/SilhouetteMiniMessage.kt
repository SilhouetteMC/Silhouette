package com.github.silhouettemc.util.text

import com.github.silhouettemc.util.ConfigUtil
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags

class SilhouetteMiniMessage {

    fun build(): MiniMessage {
        val format = mutableListOf<TagResolver>()

        val colors = ConfigUtil.messages.getTable("colors").toMap()

        fun addDefaultColorCode(code: String, color: String) {
            if (colors.contains(code)) return
            format.add(createBasicColorResolver(code, color))
        }

        addDefaultColorCode("p", "#ffd4e3")
        addDefaultColorCode("s", "#ffb5cf")
        addDefaultColorCode("t", "#6b5569")

        colors.keys.forEach {
           format.add(createBasicColorResolver(it, colors[it].toString()))
        }

        val resolvers = TagResolver.resolver(
            StandardTags.defaults(),
            *format.toTypedArray()
        )

        val builder = MiniMessage.builder()
            .tags(resolvers)

        return builder.build()
    }

    private fun createBasicColorResolver(name: String, color: String) =
        TagResolver.resolver(name, Tag.styling(TextColor.fromHexString(color)!!))

}
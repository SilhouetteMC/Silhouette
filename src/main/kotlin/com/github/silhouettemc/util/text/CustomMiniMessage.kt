package com.github.silhouettemc.util.text

import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags

class CustomMiniMessage {

    fun build(): MiniMessage {
        val resolvers = TagResolver.resolver(
            StandardTags.defaults(),
            createBasicColorResolver("p", "#ffd4e3"),
            createBasicColorResolver("s", "#ffb5cf"),
        )

        val builder = MiniMessage.builder()
            .tags(resolvers)

        return builder.build()
    }

    private fun createBasicColorResolver(name: String, color: String): TagResolver {
        return TagResolver.resolver(name, Tag.styling(TextColor.fromHexString(color)!!))
    }

}
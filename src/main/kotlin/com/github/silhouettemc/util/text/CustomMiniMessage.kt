package com.github.silhouettemc.util.text

import com.github.silhouettemc.util.ConfigUtil
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags

class CustomMiniMessage {

    fun build(): MiniMessage {
        val primary = ConfigUtil.messages.getString("primaryColour")
            ?: "#ffd4e3"
        val secondary = ConfigUtil.messages.getString("secondaryColour")
            ?: "#ffb5cf"

        val resolvers = TagResolver.resolver(
            StandardTags.defaults(),
            createBasicColorResolver("p", primary),
            createBasicColorResolver("s", secondary)
        )

        val builder = MiniMessage.builder()
            .tags(resolvers)

        return builder.build()
    }

    private fun createBasicColorResolver(name: String, color: String): TagResolver {
        return TagResolver.resolver(name, Tag.styling(TextColor.fromHexString(color)!!))
    }

}
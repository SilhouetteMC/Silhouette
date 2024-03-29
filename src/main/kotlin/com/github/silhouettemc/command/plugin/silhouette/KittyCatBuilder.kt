package com.github.silhouettemc.command.plugin.silhouette

import com.github.silhouettemc.util.text.translate
import net.kyori.adventure.text.Component

class KittyCatBuilder {

    private val kittyCat: List<Component> = """
        |xxxxxxxxxxxxxxxxxxx
        |xxxBBxxxxxxxxxBBxxx
        |xxxBLBxxxxxxxBLBxxx
        |xxxBPLBBBBBBBLPBxxx
        |xxxBPLLDLDLDLLPBxxx
        |xxxBLLLDLDLDLLLBxxx
        |xxxBLLLLLDLLLLLLBxx
        |xxBLLLLLBLLLBLLLBxx
        |xBBDLLLLBLLLBLLDBBx
        |xxBLLLLLLLLLLLLLBxx
        |xBBDLLLLPPPLLLLDBBx
        |xxBLLLLLLPLLLLLLBxx
        |xxxBLLLLLLLLLLLBxxx
        |xxxxBBLDLLLDLBBxxxx
        |xxxxxxBBBBBBBxxxxxx
        |xxxxxxxxxxxxxxxxxxx
    """.trimMargin()
        .replace("x", "<#766a85>█</#766a85>") // background
        .replace("B", "<#323236>█</#323236>") // border
        .replace("P", "<#ffccd6>█</#ffccd6>") // pink
        .replace("L", "<#cfcfcf>█</#cfcfcf>") // light gray
        .replace("D", "<#8d8d8d>█</#8d8d8d>") // dark gray
        .lines()
        .map(::translate)

    private val lines = kittyCat.toMutableList()

    fun setLine(line: Int, text: Component): KittyCatBuilder {
        lines[line] = kittyCat[line].append(Component.space()).append(text)
        return this
    }

    fun appendLine(line: Int, text: Component): KittyCatBuilder {
        lines[line].append(text)
        return this
    }

    fun prependLine(line: Int, text: Component): KittyCatBuilder {
        lines[line] = text.append(lines[line])
        return this
    }

    fun getFullLine(index: Int) = lines[index]
    fun getSkullLine(index: Int) = kittyCat[index]

    fun build(): Component {
        var component = Component.empty()
        lines.forEachIndexed { index, line ->
            component = component.append(line)
            if (index != lines.size - 1) component = component.append(Component.newline())
        }
        return component
    }

}
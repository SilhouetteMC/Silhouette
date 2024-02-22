package com.github.silhouettemc.punishment

enum class PunishmentType(val actionName: String, val displayName: String = actionName, val punishedName: String) {
    BAN("Ban", punishedName = "Banned"),
    KICK("Kick", punishedName = "Kicked"),
    MUTE("Mute", punishedName = "Muted"),
    WARN("Warn", "Warning", "Warned")
}
package com.github.silhouettemc.punishment

enum class PunishmentType(
    val actionName: String,
    val punishedName: String,
    val displayName: String = actionName,
    val shouldDisconnect: Boolean = false
) {
    BAN("Ban", "Banned", shouldDisconnect = true),
    UNBAN("Unban", "Unbanned"),
    KICK("Kick", "Kicked", shouldDisconnect = true),
    MUTE("Mute", "Muted"),
    UNMUTE("Unmute", "Unmuted"),
    WARN("Warn", "Warned", "Warning")
}
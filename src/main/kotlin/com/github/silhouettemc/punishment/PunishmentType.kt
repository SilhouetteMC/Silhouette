package com.github.silhouettemc.punishment

enum class PunishmentType(
    val actionName: String,
    val punishedName: String,
    val doingName: String,
    val displayName: String = actionName,
    val shouldDisconnect: Boolean = false
) {
    ALL("All", "All", "All punishments"),

    BAN("Ban", "Banned", "Banning", shouldDisconnect = true),
    KICK("Kick", "Kicked", "Kicking", shouldDisconnect = true),
    MUTE("Mute", "Muted", "Muting"),
    WARN("Warn", "Warned", "Warning", "Warning"),

    UNBAN("Unban", "Unbanned", "Unbanning"),
    UNMUTE("Unmute", "Unmuted", "Unmuting"),
}
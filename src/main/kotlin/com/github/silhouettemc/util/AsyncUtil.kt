package com.github.silhouettemc.util

import com.github.silhouettemc.Silhouette
import org.bukkit.scheduler.BukkitTask
import java.util.function.Consumer

fun sync(code: Consumer<BukkitTask>) {
    Silhouette.getInstance().server.scheduler.runTask(Silhouette.getInstance(), code)
}
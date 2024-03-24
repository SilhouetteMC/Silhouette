import com.github.silhouettemc.Silhouette
import org.bstats.bukkit.Metrics

object BstatsBuilder {

    private val bstatsId = 21415
    private lateinit var bstats: Metrics

    fun build(plugin: Silhouette) {
        bstats = Metrics(plugin, bstatsId)
    }

}
package com.github.silhouettemc.parsing

import com.destroystokyo.paper.profile.PlayerProfile
import com.github.silhouettemc.util.text.toUUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import org.bukkit.Bukkit

class PlayerProfileRetriever(val name: String) {

    /**
     * Makes an API call to the Mojang API to fetch the player's UUID and attempts to create a GameProfile instance. ps: fuck you mojang.
     * @return the PlayerProfile instance if successful, otherwise null
     */
    suspend fun fetchOfflinePlayerProfile(): PlayerProfile? {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.mojang.com/users/profiles/minecraft/$name")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@use null

                val body = response.body
                    ?: return@use null

                val profileResponse = Json.decodeFromString<ProfileResponse>(body.string())

                val uuidString = profileResponse.id
                val nameString = profileResponse.name

                val profile = Bukkit.createProfile(uuidString.toUUID(), nameString)
                profile
            }
        }
    }

    @Serializable
    private data class ProfileResponse(
        val id: String,
        val name: String
    )

}
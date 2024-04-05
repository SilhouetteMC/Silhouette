package com.github.silhouettemc.database.impl.mongo

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.database.Database
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.ConfigUtil
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoCollection
import com.mongodb.kotlin.client.MongoDatabase
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.conversions.Bson
import java.util.*


class MongoDatabaseImpl(
    private val plugin: Silhouette
): Database {

    private lateinit var database: MongoDatabase
    private lateinit var client: MongoClient
    private lateinit var punishmentsCollection: MongoCollection<Punishment>
    override suspend fun initialize(plugin: Silhouette) {
        plugin.launch(plugin.asyncDispatcher) {
            val databaseURI = ConfigUtil.config.getString("database.uri")
                ?: "mongodb://localhost:27017"

            val connectionString = ConnectionString(databaseURI)
            val pojoCodecRegistry: CodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build())
            val codecRegistry: CodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry
            )

            val settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(org.bson.UuidRepresentation.STANDARD)
                .codecRegistry(codecRegistry)
                .retryWrites(true)
                .build()

            client = MongoClient.create(settings)
            database = client.getDatabase("Silhouette")

            punishmentsCollection = database.getCollection("Punishments")
        }
    }

    override suspend fun addPunishment(punishment: Punishment) {
        plugin.launch(plugin.asyncDispatcher) {
            punishmentsCollection.insertOne(punishment)
        }
    }

    override suspend fun updatePunishment(punishment: Punishment, vararg updates: Bson) {
        plugin.launch(plugin.asyncDispatcher) {
            val filter = Filters.eq("id", punishment.id)
            updates.forEach {
                punishmentsCollection.updateOne(filter, it)
            }
        }
    }

    override suspend fun removePunishment(punishment: Punishment) {
        TODO("Not yet implemented")
    }

    override suspend fun listPunishments(player: UUID): List<Punishment> {
        return punishmentsCollection.find(Filters.eq("player", player)).toList()
    }

    override suspend fun getLatestActivePunishment(player: UUID, type: PunishmentType): Punishment? {
        val doc = punishmentsCollection.find(
            Filters.and(
                Filters.eq("player", player),
                Filters.eq("type", type),
                Filters.not(Filters.exists("revoker")),
                Filters.or(
                    Filters.not(Filters.exists("expiration")),
                    Filters.gt("expiration", Date())
                )
            )
        ).sort(Document("punishedOn", -1)).firstOrNull()

        return doc
    }

}
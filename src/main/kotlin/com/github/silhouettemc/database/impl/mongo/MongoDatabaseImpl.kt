package com.github.silhouettemc.database.impl.mongo

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.database.Database
import com.github.silhouettemc.punishment.Punishment
import com.github.silhouettemc.punishment.PunishmentType
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
import java.util.*


class MongoDatabaseImpl: Database {

    private lateinit var database: MongoDatabase
    private lateinit var client: MongoClient
    private lateinit var punishmentsCollection: MongoCollection<Punishment>
    override fun initialize(plugin: Silhouette) {

        val connectionString = ConnectionString("mongodb://localhost:27017") // todo: configgy
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

    override fun addPunishment(punishment: Punishment) {
        punishmentsCollection.insertOne(punishment)
    }

    override fun updatePunishment(punishment: Punishment) {
        TODO("Not yet implemented")
    }

    override fun removePunishment(punishment: Punishment) {
        TODO("Not yet implemented")
    }

    override fun listPunishments(player: UUID): List<Punishment> {
        TODO("Not yet implemented")
    }

    override fun getLatestActivePunishment(player: UUID): Punishment? {
        val doc = punishmentsCollection.find(
            Filters.and(
                Filters.eq("player", player),
                Filters.eq("type", PunishmentType.BAN),
                Filters.or(
                    Filters.not(Filters.exists("expiration")),
                    Filters.gt("expiration", Date())
                )
            )
        ).sort(Document("punishedOn", -1)).firstOrNull()

        println(doc)
        println(doc?.punishedOn)

        return doc
    }

}
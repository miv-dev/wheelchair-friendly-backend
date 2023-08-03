package dev.miv.services

import dev.miv.db.entities.CoordinatesEntity
import dev.miv.db.entities.PlaceEntity
import dev.miv.db.entities.TagEntity
import dev.miv.db.entities.UserEntity
import dev.miv.db.tables.*
import dev.miv.models.Place
import io.ktor.client.utils.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class PlaceService {

    init {
        transaction {
            SchemaUtils.create(PlaceTable, CoordinatesTable, TagTable, PlaceTagTable, UserTable, TokensTable)
        }
    }

    suspend fun all() = newSuspendedTransaction {
        PlaceEntity.all().map { placeEntity ->
            placeEntity.toDomain()
        }
    }

    suspend fun add(place: Place): Result<Unit> = newSuspendedTransaction {

        try {


            val tags = mutableListOf<TagEntity>()
            place.tags.forEach {
                tags.add(TagEntity[it.id])
            }
            val user = UserEntity[place.creator.uuid!!]


            PlaceEntity.new {
                coordinates = CoordinatesEntity.new {
                    latitude = place.coordinates.latitude
                    longitude = place.coordinates.longitude
                }
                lastUpdate = LocalDateTime.now()
                this.creator = user
                this.tags = SizedCollection(tags)
            }
        }catch (e: Exception){
            return@newSuspendedTransaction Result.failure(e)
        }


        Result.success(Unit)
    }

}


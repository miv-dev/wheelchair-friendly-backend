package dev.miv.db.entities

import dev.miv.db.tables.*
import dev.miv.models.*
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PlaceEntity(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, PlaceEntity>(PlaceTable)

    var name by PlaceTable.name
    var address by PlaceTable.address
    var lastUpdate by PlaceTable.lastUpdate
    var tags by TagEntity via PlaceTagTable
    var coordinates by CoordinatesEntity referencedOn PlaceTable.coordinates
    val reviews by ReviewEntity referrersOn ReviewTable.place
    var creator by UserEntity referencedOn PlaceTable.creator


    fun toDomain() = Place(
        id.value,
        name,
        address,
        coordinates.toDomain(),
        reviews.map { it.toDomain() }.average { it.rating },
        lastUpdate.toKotlinLocalDateTime(),
        creator.toDomain(),
    )
}

class CoordinatesEntity(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, CoordinatesEntity>(CoordinatesTable)

    var latitude by CoordinatesTable.latitude
    var longitude by CoordinatesTable.longitude

    fun toDomain() = Coordinates(latitude, longitude)

}


fun <T> List<T>.average(
    block: (T) -> Int
): Double {
    return sumOf { block(it) }.div(size).toDouble()
}




class ReviewEntity(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, ReviewEntity>(ReviewTable)

    var place by PlaceEntity referencedOn ReviewTable.place
    var title by ReviewTable.title
    var description by ReviewTable.description
    var rating by ReviewTable.rating
    var createdAt by ReviewTable.createdAt
    var accessibilityFeedback by ReviewTable.accessibilityFeedback
    var recommendation by ReviewTable.recommendation
    var user by UserEntity referencedOn ReviewTable.user
    fun toDomain() = Review(
        id.value,
        user.toDomain(),
        createdAt.toKotlinLocalDateTime(),
        title,
        description,
        accessibilityFeedback,
        recommendation,
        0,
        rating
    )
}

class UserEntity(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, UserEntity>(UserTable)

    var username by UserTable.username
    var password by UserTable.password
    var email by UserTable.email
    val reviews by ReviewEntity referrersOn ReviewTable.user
    val places by PlaceEntity referrersOn PlaceTable.creator

    fun toDomain() =
        User(
            id.value,
            username,
            email,
            password,
        )

}

class TagEntity(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, TagEntity>(TagTable)

    var name by TagTable.name

    fun toDomain() = Tag(id = id.value, name = name)

}

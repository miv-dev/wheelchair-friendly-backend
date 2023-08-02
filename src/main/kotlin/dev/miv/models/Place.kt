package dev.miv.models


import dev.miv.serializers.UUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.*



@Serializable
data class Place(
        @Serializable(with = UUIDSerializer::class)
        var id: UUID = UUID.randomUUID(),
        var name: String,
        var address: String,
        var coordinates: Coordinates,
        var rating: Double,
        var lastUpdate: LocalDateTime,
        var creator: User,
        var tags: List<Tag> = emptyList()
)

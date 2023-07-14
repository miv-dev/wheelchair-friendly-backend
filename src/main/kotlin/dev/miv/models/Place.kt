package dev.miv.models

import java.util.UUID

data class Place(
    var id: UUID,
    var coordinates: Coordinates,
    var averageScore: Double,
    var lastUpdate: Int
)

data class Coordinates(
    var latitude: Double,
    var longitude: Double
)

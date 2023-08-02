package dev.miv.models

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
        var latitude: Double,
        var longitude: Double
)

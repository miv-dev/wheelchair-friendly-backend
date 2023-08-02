package dev.miv.models

import dev.miv.serializers.UUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Review(
    @Serializable(with = UUIDSerializer::class)
    var id: UUID,
    var user: User,
    var createAt: LocalDateTime,
    var title: String,
    var text: String,
    var accessibilityFeedback:String,
    var recommendation: String? = null,
    var helpfulVotes: Int,
    var rating: Int,
)

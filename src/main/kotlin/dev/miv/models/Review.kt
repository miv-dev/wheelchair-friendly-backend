package dev.miv.models

import java.util.UUID

data class Review(
    var id: UUID,
    var user: User,
    var comment: String,
    var rate: Int,
)

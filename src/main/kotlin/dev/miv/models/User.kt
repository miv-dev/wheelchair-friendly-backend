package dev.miv.models

import kotlinx.serialization.Serializable
import java.util.*

data class User(
    val id: UUID? = null,
    val username: String = "",
    val password: String,
    val email: String,
    val isAdmin: Boolean = false,
)

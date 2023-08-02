package dev.miv.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable


object TokensTable: IntIdTable("refresh_tokens") {
    val userId = uuid("userId")
    val refreshToken = varchar("refreshToken", 300)
    val expiresAt = long("expiresAt")
}

package dev.miv.db.tables

import dev.miv.db.tables.PlaceTable.default
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Table.Dual.default
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object PlaceTable : UUIDTable(name = "place_table") {
    val name = varchar("name", length = 255)
    val address = varchar("address", length = 255)
    val creator = reference("creator", UserTable)
    val coordinates = reference("coordinates", CoordinatesTable, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val lastUpdate = datetime("last_update")
    val createdAt = datetime("created_at")
}

object CoordinatesTable : IntIdTable(name = "coordinates_table") {
    val latitude = double("latitude")
    val longitude = double("longitude")
}

object PlaceTagTable : Table(name = "place__tag"){
    val place = reference("palace", PlaceTable)
    val tag = reference("tag", TagTable)
}

object TagTable: IntIdTable(name = "tag_table"){
    val name = varchar("name", 100).uniqueIndex()
}

object ReviewTable: UUIDTable(name="review_table"){
    val place = reference("place", PlaceTable, onDelete = ReferenceOption.CASCADE)
    val user = reference("user", UserTable)
    val title = varchar("title", 100)
    val description = varchar("description", 5000)
    val recommendation = varchar("recommendation", 5000)
    val accessibilityFeedback = varchar("accessibility_feedback", 5000)
    val rating = integer("rating").default(0)
    val createdAt = datetime("created_at")
}
object UserTable: UUIDTable(name = "user_table"){
    val username = varchar("username", 64)
    val password = varchar("password", 64)
    val email = varchar("email", 64).uniqueIndex()

}


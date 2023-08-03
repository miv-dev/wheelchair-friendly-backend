package dev.miv.services

import dev.miv.db.entities.UserEntity
import dev.miv.db.tables.UserTable
import dev.miv.models.User
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserService {
    init {
        transaction {
        }
    }

    suspend fun userByEmail(email: String): User? = newSuspendedTransaction {
        UserEntity.find { UserTable.email eq email }.firstOrNull()?.toDomain()
    }

    suspend fun userByUsername(username: String): User? = newSuspendedTransaction {
        if (username == "") null
        else UserEntity.find { UserTable.username eq username }.firstOrNull()?.toDomain()
    }

    suspend fun userById(userId: String): User = newSuspendedTransaction {
        UserEntity[UUID.fromString(userId)].toDomain()
    }

    suspend fun login(email: String, password: String): User {
        userByEmail(email)?.let { user: User ->

            if (user.password == password) {
                return user
            } else {
                throw RuntimeException("Credentials aren't valid")
            }

        } ?: throw RuntimeException("User is not exist")
    }

    suspend fun users(): List<User> = newSuspendedTransaction {
        UserEntity.all().map { it.toDomain() }
    }


    suspend fun create(user: User): Result<User> = newSuspendedTransaction {
        try {
            val newUser = UserEntity.new {
                username = user.username
                password = user.password
                email = user.email
            }.toDomain()

            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun delete(uuids: List<String>) = newSuspendedTransaction {
        uuids.forEach { uuid ->
            UserEntity[UUID.fromString(uuid)].delete()
        }
    }

    suspend fun update(user: User) = newSuspendedTransaction {
        if (user.uuid != null) {
            UserEntity[user.uuid].apply {
                email = user.email
                password = user.password
                username = user.username

            }
        }
    }
}

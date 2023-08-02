package dev.miv.routing

import dev.miv.models.LoginRequest
import dev.miv.models.RefreshToken
import dev.miv.models.TokenPair
import dev.miv.models.User
import dev.miv.services.TokenService
import dev.miv.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@kotlinx.serialization.Serializable
data class LoginResponse(
    val tokenPair: TokenPair,
    val user: User
)

@kotlinx.serialization.Serializable
data class FailResponse(
    val result: String,
    val error: String
)

fun Route.authRouting(userService: UserService, tokenService: TokenService) {
    route("/auth") {
        post("/refresh") {
            val oldRT = call.receive<RefreshToken>().refreshToken // old refresh token
            val token = tokenService.find(oldRT)

            val currentTime = System.currentTimeMillis()

            if (token != null && token.expiresAt > currentTime) {
                val tokenPair = tokenService.generateTokenPair(token.uuid, true)

                tokenService.updateByRefreshToken(
                    oldRT,
                    tokenPair.refreshToken,
                    currentTime
                )
                call.respond(tokenPair)
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    hashMapOf("description" to "invalid token"),
                )
            }
        }

        post("/login") {
            val authUser = call.receive<LoginRequest>()

            val user = userService.userByEmail(authUser.email)

            if (user != null && authUser.password == user.password && authUser.email == user.email) {
                val tokenPair = tokenService.generateTokenPair(user.uuid!!)
                call.respond(
                    LoginResponse(tokenPair, user)
                )
            } else
                call.respond(HttpStatusCode.Unauthorized)
        }
        post("/register") {
            val newUser = call.receive<User>()

            val result = userService.create(newUser)
            result.fold(
                onSuccess = { user ->
                    val tokenPair = tokenService.generateTokenPair(user.uuid!!)
                    call.respond(
                        LoginResponse(tokenPair, user)
                    )
                },
                onFailure = {
                    call.respond(
                        FailResponse(
                            "fail",
                            it.localizedMessage
                        )
                    )
                }
            )


        }
    }
}

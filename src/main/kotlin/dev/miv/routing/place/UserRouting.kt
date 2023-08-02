package dev.miv.routing.place

import dev.miv.services.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.user(userService: UserService) {
    route("/users") {
        authenticate("access") {
            get("/current") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                userService.userById(userId).let {
                    call.respond(it)
                }
            }

            get {
                userService.users().let {
                    call.respond(it)
                }
            }
        }
    }
}

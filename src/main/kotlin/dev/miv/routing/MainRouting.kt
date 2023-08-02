package dev.miv.routing

import dev.miv.routing.place.place
import dev.miv.routing.place.user
import dev.miv.services.PlaceService
import dev.miv.services.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.mainRouting(){
    val placeService = PlaceService()
    val userService = UserService()
    routing {
        route("/api"){
            place(placeService)
            user(userService)
        }
    }
}

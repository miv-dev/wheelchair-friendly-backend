package dev.miv.routing.place

import dev.miv.models.Place
import dev.miv.services.PlaceService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.place(placeService: PlaceService) {
    route("/place") {
        get {
            call.respond(placeService.all())
        }
        post {
            try {
                val place = call.receive<Place>()

                val result = placeService.add(place)
                result.fold({
                    call.respond(HttpStatusCode.OK)
                }, {
                    call.respond(
                            HttpStatusCode.InternalServerError,
                            mapOf(
                                    "success" to false,
                                    "error" to "${it.message}"
                            ),
                    )
                })

            } catch (e: CannotTransformContentToTypeException) {
                call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf(
                                "success" to false,
                                "error" to "${e.message}"
                        ),
                )
            }


        }
    }
}

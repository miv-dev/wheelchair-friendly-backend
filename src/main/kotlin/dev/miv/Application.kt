package dev.miv

import com.auth0.jwt.algorithms.Algorithm
import dev.miv.db.Credentials
import dev.miv.db.DBConfig
import dev.miv.routing.authRouting
import dev.miv.routing.mainRouting
import dev.miv.services.TokenService
import dev.miv.services.UserService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.stringProperty(path: String): String =
    this.environment.config.property(path).getString()

fun Application.longProperty(path: String): Long =
    stringProperty(path).toLong()

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    initDB()


    install(ContentNegotiation) {
        json()
    }
    val userService = UserService()

    authenticationPlugin {
        authRouting(tokenService = it, userService = userService)
    }
    mainRouting()
}

fun Application.authenticationPlugin(routing: Route.(tokenService: TokenService) -> Unit) {
    val tokenService = TokenService(
        stringProperty("jwt.issuer"),
        Algorithm.HMAC256(stringProperty("jwt.access.secret")),
        longProperty("jwt.access.lifetime"),
        longProperty("jwt.refresh.lifetime")
    )

    install(Authentication) {
        jwt("access") {
            verifier {
                tokenService.makeJWTVerifier()
            }
            challenge { defaultScheme, realm ->
                println(defaultScheme)
                println(realm)
            }
            validate { token ->
                if (token.payload.expiresAt.time > System.currentTimeMillis())
                    JWTPrincipal(token.payload)
                else null
            }
        }

    }

    routing {
        routing(tokenService)
    }
}

fun Application.initDB() {
    val url = stringProperty("db.url")
    val username = stringProperty("db.username")
    val password = stringProperty("db.password")

    val credentials = Credentials(url, username, password)

    DBConfig.setup(credentials)
}

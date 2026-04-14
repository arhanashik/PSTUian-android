package com.workfort.pstuian.data.remote

import com.workfort.pstuian.appconstant.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClientFactory {
    fun create(authTokenProvider: () -> String?): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Ktor: $message")
                    }
                }
                level = LogLevel.ALL
            }

            defaultRequest {
                url(NetworkConst.Remote.BASE_API_URL)
                contentType(ContentType.Application.Json)
                authTokenProvider()?.let { token ->
                    header("x-auth-token", token)
                }
                header("Cache-Control", "no-cache")
            }
        }
    }
}

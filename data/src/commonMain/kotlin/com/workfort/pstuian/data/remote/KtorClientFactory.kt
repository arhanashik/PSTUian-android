package com.workfort.pstuian.data.remote

import com.workfort.pstuian.util.PlatformInfo
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

    /**
     * Minimal client for arbitrary absolute URLs (no API base URL, no auth headers).
     */
    fun createPlainHttpClient(platformInfo: PlatformInfo): HttpClient {
        val isDebug = platformInfo.isDebug
        val userAgent = "PSTUian/${platformInfo.appVersionName} (${platformInfo.platform}; ${platformInfo.model})"
        return HttpClient {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Ktor: $message")
                    }
                }
                level = if (isDebug) LogLevel.ALL else LogLevel.NONE
            }

            defaultRequest {
                header("User-Agent", userAgent)
                header("Cache-Control", "no-cache")
            }
        }
    }

    fun create(
        platformInfo: PlatformInfo,
        baseUrlProvider: () -> String,
        authTokenProvider: () -> String?,
    ): HttpClient {
        val isDebug = platformInfo.isDebug
        val userAgent = "PSTUian/${platformInfo.appVersionName} (${platformInfo.platform}; ${platformInfo.model})"
        return HttpClient {
            install(ContentNegotiation) {
                val json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
                json(json, contentType = ContentType.Application.Json)
                json(json, contentType = ContentType.Text.Html)
                json(json, contentType = ContentType.parse("text/html; charset=UTF-8"))
            }
            
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Ktor: $message")
                    }
                }
                level = if (isDebug) LogLevel.ALL else LogLevel.NONE
            }

            defaultRequest {
                url(urlString = baseUrlProvider())
                contentType(ContentType.Application.Json)
                header("User-Agent", userAgent)
                header("x-auth-token", authTokenProvider())
                header("Cache-Control", "no-cache")
            }
        }
    }
}

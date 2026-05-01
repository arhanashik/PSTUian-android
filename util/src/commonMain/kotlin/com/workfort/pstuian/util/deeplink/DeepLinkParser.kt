package com.workfort.pstuian.util.deeplink

class DeepLinkParser {

    fun parse(url: String?): DeepLinkAction? {
        val parsed = parseUrl(url ?: return null) ?: return null
        val target = resolveTarget(parsed) ?: return null
        return when (target.lowercase()) {
            "auth" -> parseAuthDeepLink(parsed)
            "profile" -> parseProfileDeepLink(parsed)
            else -> null
        }
    }

    private fun resolveTarget(uri: ParsedUrl): String? {
        return when {
            uri.scheme.equals("pstuian", ignoreCase = true) -> uri.host
            uri.scheme.isWebScheme() && uri.host.isSupportedWebHost() ->
                uri.path.substringBefore('/')
            else -> null
        }
    }

    private fun parseAuthDeepLink(uri: ParsedUrl): DeepLinkAction? {
        if (uri.path.isNotBlank()) return null
        val params = parsePairs(uri.query, '&')
        if (!params.firstCaseInsensitiveValue("action").equals("resetPassword", ignoreCase = true)) return null
        return DeepLinkAction.ResetPassword(
            params = ResetPasswordParams(
                mode = params.firstCaseInsensitiveValue("mode"),
                oobCode = params.firstCaseInsensitiveValue("oobCode")
                    ?: params.firstCaseInsensitiveValue("oob_code"),
                apiKey = params.firstCaseInsensitiveValue("apiKey")
                    ?: params.firstCaseInsensitiveValue("api_key"),
                continueUrl = params.firstCaseInsensitiveValue("continueUrl")
                    ?: params.firstCaseInsensitiveValue("continue_url"),
                languageCode = params.firstCaseInsensitiveValue("languageCode")
                    ?: params.firstCaseInsensitiveValue("lang"),
                tenantId = params.firstCaseInsensitiveValue("tenantId")
                    ?: params.firstCaseInsensitiveValue("tenant_id"),
            ),
        )
    }

    private fun parseProfileDeepLink(uri: ParsedUrl): DeepLinkAction? {
        val params = parsePairs(uri.query, '&').ifEmpty { parsePairs(uri.path, '&') }
        val userId = params.firstCaseInsensitiveValue("userId")?.toIntOrNull() ?: return null
        val userTypeRaw = params.firstCaseInsensitiveValue("userType") ?: return null
        return DeepLinkAction.OpenProfile(userId = userId, userTypeRaw = userTypeRaw)
    }

    private fun parseUrl(rawUrl: String): ParsedUrl? {
        val schemeIndex = rawUrl.indexOf("://")
        if (schemeIndex <= 0) return null
        val scheme = rawUrl.substring(0, schemeIndex)
        val afterScheme = rawUrl.substring(schemeIndex + 3)
        val queryIndex = afterScheme.indexOf('?')
        val withoutQuery = if (queryIndex >= 0) afterScheme.substring(0, queryIndex) else afterScheme
        val query = if (queryIndex >= 0) afterScheme.substring(queryIndex + 1) else ""
        val slashIndex = withoutQuery.indexOf('/')
        val host = if (slashIndex >= 0) withoutQuery.substring(0, slashIndex) else withoutQuery
        val path = if (slashIndex >= 0) withoutQuery.substring(slashIndex + 1) else ""
        if (host.isBlank()) return null
        return ParsedUrl(scheme = scheme, host = host, path = path.trim('/'), query = query)
    }

    private fun parsePairs(raw: String, delimiter: Char): Map<String, String> {
        if (raw.isBlank()) return emptyMap()
        val out = LinkedHashMap<String, String>()
        raw.split("&")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { part ->
                val idx = part.indexOf('=')
                if (idx < 0) {
                    out[part] = ""
                    return@forEach
                }
                val key = part.substring(0, idx)
                if (key.isBlank()) return@forEach
                val value = part.substring(idx + 1)
                out[key] = value
            }
        return out
    }
}

private data class ParsedUrl(
    val scheme: String,
    val host: String,
    val path: String,
    val query: String,
)

private fun Map<String, String>.firstCaseInsensitiveValue(key: String): String? =
    entries.firstOrNull { it.key.equals(key, ignoreCase = true) }?.value

private fun String.isWebScheme(): Boolean =
    equals("https", ignoreCase = true)

private fun String.isSupportedWebHost(): Boolean =
    equals("dev.pstuian.com", ignoreCase = true) || equals("pstuian.com", ignoreCase = true)

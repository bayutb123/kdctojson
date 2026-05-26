package com.github.bayutb123.kdctojson.utils

/**
 * Thrown when Gemini HTTP response status is not successful (non-2xx).
 */
class GeminiNonOkResponseException(
    val statusCode: Int,
    val responseBody: String
) : Exception("Gemini response was not successful. HTTP $statusCode")
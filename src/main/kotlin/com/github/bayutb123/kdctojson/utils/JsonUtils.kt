package com.github.bayutb123.kdctojson.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Format string if it was json for better readability
 */
fun String.formatJson(): String {
    return try {
        val json = Json { prettyPrint = true; isLenient = true }
        val jsonElement = json.parseToJsonElement(this)
        json.encodeToString(JsonElement.serializer(), jsonElement)
    } catch (e: Exception) {
        this
    }
}
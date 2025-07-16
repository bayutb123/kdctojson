package com.github.bayutb123.kdctojson.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Format the string if it is valid JSON for improved readability.
 */
@OptIn(ExperimentalSerializationApi::class)
fun String.formatJson(): String {
    return try {
        val json = Json { 
            prettyPrint = true
            isLenient = true
            prettyPrintIndent = "    " // 4 spaces for indentation
        }
        val jsonElement = json.parseToJsonElement(this)
        json.encodeToString(JsonElement.serializer(), jsonElement)
    } catch (e: Exception) {
        this
    }
}
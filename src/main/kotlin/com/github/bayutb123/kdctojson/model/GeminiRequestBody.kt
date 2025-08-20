package com.github.bayutb123.kdctojson.model

import kotlinx.serialization.Serializable

@Serializable
data class Part(val text: String)

@Serializable
data class Content(val role: String, val parts: List<Part>)

@Serializable
data class GenerationConfig(
    val temperature: Double,
    val topK: Int,
    val topP: Double,
    val maxOutputTokens: Int,
    val responseMimeType: String
)

@Serializable
data class SafetySetting(val category: String, val threshold: String)

@Serializable
data class GeminiRequestBody(
    val contents: List<Content>,
    val generationConfig: GenerationConfig,
    val safetySettings: List<SafetySetting>
)
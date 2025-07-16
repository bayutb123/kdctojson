package com.github.bayutb123.kdctojson.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null,
    @SerialName("usageMetadata") val usageMetadata: UsageMetadata? = null,
    val error: GeminiError? = null
)

@Serializable
data class Candidate(
    val content: Content,
    val finishReason: String? = null,
    val index: Int? = null,
    val safetyRatings: List<SafetyRating>? = null
)

@Serializable
data class Content(
    val parts: List<Part>,
    val role: String? = null
)

@Serializable
data class Part(
    val text: String
)

@Serializable
data class UsageMetadata(
    @SerialName("promptTokenCount") val promptTokenCount: Int,
    @SerialName("candidatesTokenCount") val candidatesTokenCount: Int? = null,
    @SerialName("totalTokenCount") val totalTokenCount: Int
)

@Serializable
data class SafetyRating(
    val category: String,
    val probability: String
)

@Serializable
data class GeminiError(
    val code: Int,
    val message: String,
    val status: String? = null
)

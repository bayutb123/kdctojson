package com.github.bayutb123.kdctojson.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>,
    @SerialName("usageMetadata") val usageMetadata: UsageMetadata? = null // Optional
)

@Serializable
data class Candidate(
    val content: Content,
    @SerialName("finishReason") val finishReason: String,
    val index: Int,
    @SerialName("groundingMetadata") val groundingMetadata: GroundingMetadata? = null // Optional
)

@Serializable
data class Content(
    val parts: List<Part>,
    val role: String
)

@Serializable
data class Part(
    val text: String
)

@Serializable
data class UsageMetadata(
    @SerialName("promptTokenCount") val promptTokenCount: Int,
    @SerialName("candidatesTokenCount") val candidatesTokenCount: Int,
    @SerialName("totalTokenCount") val totalTokenCount: Int
)

@Serializable
data class GroundingMetadata(
    val placeholder: String? = null
)
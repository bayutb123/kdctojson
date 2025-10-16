@file:Suppress("UnstableApiUsage")

package com.github.bayutb123.kdctojson.utils

import com.github.bayutb123.kdctojson.remote.request.Content
import com.github.bayutb123.kdctojson.remote.request.GeminiRequestBody
import com.github.bayutb123.kdctojson.remote.request.GenerationConfig
import com.github.bayutb123.kdctojson.remote.request.Part
import com.github.bayutb123.kdctojson.remote.request.SafetySetting
import com.intellij.openapi.project.Project
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.kotlin.psi.KtCallableDeclaration
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtEnumEntry

object JSONGenerator {
    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 300_000
            socketTimeoutMillis = 300_000
            connectTimeoutMillis = 300_000
        }
    }

    fun generateSampleJson(
        project: Project,
        dataClass: KtClass,
        indentLevel: Int = 0
    ): String {
        val constructorProps = dataClass.primaryConstructor?.valueParameters?.filter { it.hasValOrVar() } ?: emptyList()
        val bodyProps = dataClass.getBody()?.properties ?: emptyList()
        val allProps: List<KtCallableDeclaration> = constructorProps + bodyProps

        val fieldIndent = " ".repeat(4 * (indentLevel + 1))
        val closingBraceIndent = " ".repeat(4 * indentLevel)

        val jsonFields = allProps.mapNotNull { prop ->
            val name = prop.name ?: return@mapNotNull null
            val typeText = prop.typeReference?.text ?: "Any"

            val value = generateValueForTypeText(project, dataClass, typeText, indentLevel + 1)

            "$fieldIndent\"$name\": $value"
        }.joinToString(",\n")

        return "{\n$jsonFields\n$closingBraceIndent}"
    }

    private fun generateValueForTypeText(
        project: Project,
        contextClass: KtClass,
        rawTypeText: String,
        indentLevel: Int
    ): String {
        val typeText = rawTypeText.trim().removeSuffix("?")
        val closingBracketIndent = " ".repeat(4 * (indentLevel - 1))

        return when {
            typeText == "String" || typeText == "kotlin.String" -> "\"example\""
            typeText == "Int" || typeText == "kotlin.Int" -> "0"
            typeText == "Boolean" || typeText == "kotlin.Boolean" -> "true"
            typeText == "Long" || typeText == "kotlin.Long" -> "0"
            typeText == "Double" || typeText == "kotlin.Double" || typeText == "Float" || typeText == "kotlin.Float" -> "0.0"

            // Handle List<T>
            typeText.startsWith("List<") || typeText.startsWith("kotlin.collections.List<") -> {
                val inner = typeText.substringAfter('<').substringBeforeLast('>')
                val sampleElement = generateValueForTypeText(project, contextClass, inner, indentLevel)
                val isComplexElement = sampleElement.trimStart().startsWith("{")
                val elements = List(3) { sampleElement }
                if (isComplexElement) {
                    "[\n" + elements.joinToString(",\n") + "\n$closingBracketIndent]"
                } else {
                    "[ " + elements.joinToString(", ") + " ]"
                }
            }

            // Other collections → empty array
            typeText.startsWith("Collection<") || typeText.startsWith("Set<") ||
                    typeText.startsWith("MutableList<") || typeText.startsWith("MutableSet<") ||
                    typeText.startsWith("MutableCollection<") ||
                    typeText.startsWith("kotlin.collections.Collection<") ||
                    typeText.startsWith("kotlin.collections.Set<") ||
                    typeText.startsWith("kotlin.collections.MutableList<") ||
                    typeText.startsWith("kotlin.collections.MutableSet<") ||
                    typeText.startsWith("kotlin.collections.MutableCollection<") -> "[]"

            else -> {
                // Try to resolve to a class in the same file for data classes/enums
                val shortName = typeText.substringAfterLast('.')
                val ktFile = contextClass.containingKtFile
                val target = ktFile.declarations.filterIsInstance<KtClass>().firstOrNull { it.name == shortName }
                when {
                    target == null -> "null"
                    target.isData() -> generateSampleJson(project, target, indentLevel)
                    target.isEnum() -> {
                        val firstEntryName = target.declarations.filterIsInstance<KtEnumEntry>().firstOrNull()?.name
                        firstEntryName?.let { "\"$it\"" } ?: "\"ENUM_VALUE\""
                    }
                    else -> "null"
                }
            }
        }
    }

    suspend fun generateJsonWithGemini(apiKey: String, dataClassText: String, model: String): String {
        val prompt = """
        Generate a realistic, sample JSON object based on the following Kotlin data class.
        The JSON should be populated with plausible, diverse, and realistic data from indonesia.
        Do not include any explanations, comments, or markdown code fences in your response.
        Only output the raw JSON object itself.

        Data Class Definition:
        ```kotlin
        $dataClassText
        ```
    """.trimIndent()

        val responseMimeType = when (model) {
            "gemma-3-1b-it" -> "text/plain"
            else -> "application/json"
        }

        val requestBody = GeminiRequestBody(
            contents = listOf(
                Content(
                    role = "user",
                    parts = listOf(Part(text = prompt))
                )
            ),
            generationConfig = GenerationConfig(
                temperature = 0.7,
                topK = 40,
                topP = 0.95,
                maxOutputTokens = 8192, // increased to better handle larger data classes
                responseMimeType = responseMimeType
            ),
            safetySettings = listOf(
                SafetySetting(category = "HARM_CATEGORY_HARASSMENT", threshold = "BLOCK_MEDIUM_AND_ABOVE"),
                SafetySetting(category = "HARM_CATEGORY_HATE_SPEECH", threshold = "BLOCK_MEDIUM_AND_ABOVE"),
                SafetySetting(category = "HARM_CATEGORY_SEXUALLY_EXPLICIT", threshold = "BLOCK_MEDIUM_AND_ABOVE"),
                SafetySetting(category = "HARM_CATEGORY_DANGEROUS_CONTENT", threshold = "BLOCK_MEDIUM_AND_ABOVE")
            )
        )

        val jsonString = Json.encodeToString(requestBody)

        val result = client.post("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(jsonString)
        }

        return GeminiUtils.extractTextFromResponse(result.bodyAsText()) ?: "Failed to extract Gemini Response, ${result.bodyAsText()}"
    }
}
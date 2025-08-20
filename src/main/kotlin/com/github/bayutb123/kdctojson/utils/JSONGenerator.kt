@file:Suppress("UnstableApiUsage")

package com.github.bayutb123.kdctojson.utils

import com.github.bayutb123.kdctojson.model.Content
import com.github.bayutb123.kdctojson.model.GeminiRequestBody
import com.github.bayutb123.kdctojson.model.GenerationConfig
import com.github.bayutb123.kdctojson.model.Part
import com.github.bayutb123.kdctojson.model.SafetySetting
import com.intellij.openapi.project.Project
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.psi.KtCallableDeclaration
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtEnumEntry
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode
import org.jetbrains.kotlin.resolve.source.getPsi
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.makeNotNullable

object JSONGenerator {
     private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = io.ktor.client.plugins.HttpTimeout.INFINITE_TIMEOUT_MS
            connectTimeoutMillis = io.ktor.client.plugins.HttpTimeout.INFINITE_TIMEOUT_MS
            socketTimeoutMillis = io.ktor.client.plugins.HttpTimeout.INFINITE_TIMEOUT_MS
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
            val typeReference = prop.typeReference

            val kotlinType = typeReference?.let {
                val bindingContext = it.analyze(BodyResolveMode.FULL)
                bindingContext[BindingContext.TYPE, it]
            }

            // Delegate the value generation to the recursive helper function
            val value = generateValueForType(project, kotlinType, indentLevel + 1)

            "$fieldIndent\"$name\": $value"
        }.joinToString(",\n")

        return "{\n$jsonFields\n$closingBraceIndent}"
    }

    private fun generateValueForType(
        project: Project,
        kotlinType: KotlinType?,
        indentLevel: Int
    ): String {
        // Base case for unresolved or null types
        if (kotlinType == null) return "null"

        val nonNullableType = kotlinType.makeNotNullable()
        val closingBracketIndent = " ".repeat(4 * (indentLevel - 1))

        return when {
            KotlinBuiltIns.isString(nonNullableType) -> "\"example\""
            KotlinBuiltIns.isInt(nonNullableType) -> "0"
            KotlinBuiltIns.isBoolean(nonNullableType) -> "true"

            // FIX: Handle List<T> by generating 3 elements of type T
            KotlinBuiltIns.isListOrNullableList(nonNullableType) -> {
                val genericType = nonNullableType.arguments.firstOrNull()?.type
                // Recursively generate a sample for the list's element type
                val sampleElement = generateValueForType(project, genericType, indentLevel)

                // Format the list based on whether the element is a complex object
                val isComplexElement = sampleElement.trimStart().startsWith("{")
                val elements = List(3) { sampleElement }

                if (isComplexElement) {
                    "[\n" + elements.joinToString(",\n") + "\n$closingBracketIndent]"
                } else {
                    "[ " + elements.joinToString(", ") + " ]"
                }
            }

            // Fallback for other collection types
            KotlinBuiltIns.isCollectionOrNullableCollection(nonNullableType) -> "[]"

            nonNullableType.constructor.declarationDescriptor is ClassDescriptor -> {
                val classDescriptor = nonNullableType.constructor.declarationDescriptor as ClassDescriptor
                val psiClass = classDescriptor.source.getPsi() as? KtClass

                when {
                    // If it's a data class, recurse using the main object generator
                    psiClass != null && psiClass.isData() -> {
                        generateSampleJson(project, psiClass, indentLevel)
                    }
                    // If it's an enum, get the first value
                    classDescriptor.kind == ClassKind.ENUM_CLASS -> {
                        val firstEntryName = psiClass?.declarations
                            ?.filterIsInstance<KtEnumEntry>()
                            ?.firstOrNull()
                            ?.name
                        firstEntryName?.let { "\"$it\"" } ?: "\"ENUM_VALUE\""
                    }
                    else -> "null" // Other complex classes
                }
            }
            else -> "null" // Default for unhandled types
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

        // Create an instance of your data class representing the entire request body
        val requestBody = GeminiRequestBody(
            contents = listOf(
                Content(
                    role = "user",
                    parts = listOf(Part(text = prompt)) // The 'prompt' string is correctly handled here
                )
            ),
            generationConfig = GenerationConfig(
                temperature = 0.7,
                topK = 40,
                topP = 0.95,
                maxOutputTokens = 2048,
                responseMimeType = responseMimeType
            ),
            safetySettings = listOf(
                SafetySetting(category = "HARM_CATEGORY_HARASSMENT", threshold = "BLOCK_MEDIUM_AND_ABOVE"),
                SafetySetting(category = "HARM_CATEGORY_HATE_SPEECH", threshold = "BLOCK_MEDIUM_AND_ABOVE"),
                SafetySetting(category = "HARM_CATEGORY_SEXUALLY_EXPLICIT", threshold = "BLOCK_MEDIUM_AND_ABOVE"),
                SafetySetting(category = "HARM_CATEGORY_DANGEROUS_CONTENT", threshold = "BLOCK_MEDIUM_AND_ABOVE")
            )
        )

        // Serialize the data class instance to a JSON string
        // You can customize JSON { ignoreUnknownKeys = true } etc. if needed
        val jsonString = Json.encodeToString(requestBody)

        val result = client.post("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(jsonString) // Set the pre-serialized JSON string here
        }

        return GeminiUtils.extractTextFromResponse(result.bodyAsText()) ?: "Failed to extract Gemini Response, ${result.bodyAsText()}"
    }
}
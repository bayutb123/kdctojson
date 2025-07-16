package com.github.bayutb123.kdctojson.utils

import com.github.bayutb123.kdctojson.remote.response.GeminiResponse
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import kotlinx.serialization.json.Json

const val UNDEFINED = "undefined"

/**
 * Parses a JSON string from the Gemini API and extracts the text from the first candidate's first part.
 *
 * @param jsonString The raw JSON string response from the Gemini API.
 * @return The extracted text as a String, or null if parsing fails or the structure is unexpected.
 */
fun extractTextFromGeminiResponse(jsonString: String): String? {
    // Configure the JSON parser to be lenient, ignoring unknown keys
    // which makes the parsing more robust to API changes.
    val json = Json { ignoreUnknownKeys = true }

    return try {
        // Parse the JSON string into our GeminiResponse data class structure.
        val response = json.decodeFromString<GeminiResponse>(jsonString)

        // Navigate through the parsed object to find the desired text.
        // We use safe calls (?.) to prevent null pointer exceptions if a part of the
        // structure is missing.
        response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
    } catch (e: Exception) {
        // If any error occurs during parsing (e.g., malformed JSON),
        // print the error and return null.
        println("Error parsing JSON: ${e.message}")
        null
    }
}

/**
 * Prompt user to select Gemini model or Without AI
 */
fun promptSelectModel(project: Project): String? {
    val models = arrayOf("gemini-2.5-flash", "gemini-2.5-pro", "gemini-2.5-flash-lite-preview-06-17")
    val model = Messages.showEditableChooseDialog(
        "Select model",
        "Select Gemini API",
        Messages.getQuestionIcon(),
        arrayOf("Without Gemini API") + models,
        "Without Gemini API",
        null
    )

    if (!models.contains(model)) {
        if (model != "Without Gemini API") {
            showNotification(project, "$model is not available", NotificationType.WARNING)
            return UNDEFINED
        } else {
            return null
        }
    }

    return model
}
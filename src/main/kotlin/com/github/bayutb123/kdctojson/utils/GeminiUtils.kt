package com.github.bayutb123.kdctojson.utils

import com.github.bayutb123.kdctojson.remote.response.GeminiResponse
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import kotlinx.serialization.json.Json

const val UNDEFINED = "undefined"

object GeminiUtils {
    
    /**
     * Parses a JSON string from the Gemini API and extracts the text from the first candidate's first part.
     */
    fun extractTextFromResponse(jsonString: String): String? {
        val json = Json { ignoreUnknownKeys = true }
        
        return try {
            val response = json.decodeFromString<GeminiResponse>(jsonString)
            
            // Check for API errors first
            response.error?.let { error ->
                println("Gemini API Error: ${error.message} (Code: ${error.code})")
                return null
            }
            
            // Extract text from candidates
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.cleanMarkdown()
        } catch (e: Exception) {
            println("Error parsing JSON: ${e.message}")
            println("Raw response: $jsonString")
            null
        }
    }

    /**
     * Prompts user to select a Gemini model or use local generation
     */
    fun promptModelSelection(project: Project): String? {
        val availableModels = arrayOf(
            "gemini-2.5-flash", 
            "gemini-2.5-pro", 
            "gemini-2.5-flash-lite-preview-06-17",
            "learnlm-2.0-flash-experimental",
            "gemini-2.0-flash-lite",
            "gemma-3-1b-it"
        )
        
        val selectedModel = Messages.showEditableChooseDialog(
            "Select model",
            "Select Gemini API",
            Messages.getQuestionIcon(),
            arrayOf("Without Gemini API") + availableModels,
            "Without Gemini API",
            null
        )

        return when (selectedModel) {
            "Without Gemini API" -> null
            in availableModels -> selectedModel
            else -> {
                NotificationUtils.showWarning(project, "$selectedModel is not available")
                UNDEFINED
            }
        }
    }
}

/**
 * Removes Markdown code fences and trims whitespace
 */
fun String.cleanMarkdown(): String {
    return this
        .replace("```json", "")
        .replace("```", "")
        .trim()
}
package com.github.bayutb123.kdctojson.services

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.ui.Messages

@Service(Service.Level.APP)
class GeminiCredentialManager {
    
    companion object {
        private const val SERVICE_NAME = "kdctojson"
        private const val KEY_NAME = "GEMINI_API_KEY"
        
        fun getInstance(): GeminiCredentialManager = 
            ApplicationManager.getApplication().getService(GeminiCredentialManager::class.java)
    }

    private val credentialAttributes = CredentialAttributes(SERVICE_NAME)

    fun getApiKey(): String? {
        val credentials = PasswordSafe.instance.get(credentialAttributes)
        return credentials?.getPasswordAsString()
    }

    fun promptAndSaveApiKey(): String? {
        val apiKey = Messages.showInputDialogWithCheckBox(
            "Please enter your Google AI Studio (Gemini) API Key:",
            "Gemini API Key",
            "Save API key",
            false,
            true,
            Messages.getInformationIcon(),
            null,
            null
        )

        if (apiKey.second) {
            val credentials = Credentials(KEY_NAME, apiKey.first)
            PasswordSafe.instance.set(credentialAttributes, credentials)
            return apiKey.first
        }
        return apiKey.first
    }
}
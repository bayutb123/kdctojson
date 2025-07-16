@file:Suppress("UnstableApiUsage")

package com.github.bayutb123.kdctojson

import com.github.bayutb123.kdctojson.utils.JSONGenerator
import com.github.bayutb123.kdctojson.utils.UNDEFINED
import com.github.bayutb123.kdctojson.utils.formatJson
import com.github.bayutb123.kdctojson.utils.promptSelectModel
import com.github.bayutb123.kdctojson.utils.showNotification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.psi.util.PsiTreeUtil
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.psi.KtClass
import java.awt.datatransfer.StringSelection

class GetJsonAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return

        val offset = editor.caretModel.offset
        val element = psiFile.findElementAt(offset)
        val dataClass = PsiTreeUtil.getParentOfType(element, KtClass::class.java, false)

        if (dataClass == null || !dataClass.isData()) {
            showNotification(project, "Please place the cursor inside a Kotlin data class.", NotificationType.WARNING)
            return
        }

        val model = promptSelectModel(project)
        // Get API key, prompting the user if it's not set
        var apiKey = model?.let { GeminiCredentialManager.getApiKey() }
        if (apiKey.isNullOrBlank()) {
            apiKey = if (model != null) {
                if (model == UNDEFINED) {
                    return
                } else {
                    GeminiCredentialManager.promptAndSaveApiKey()
                }
            } else null

        }

        if (apiKey == null && model != null && model != UNDEFINED) {
            return
        }

        val notifTitle = if (!model.isNullOrEmpty()) "Generating JSON with Gemini" else "Generating JSON"
        val indicatorTitle = if (!model.isNullOrEmpty()) "Asking Gemini for a JSON sample..." else ""
        // Run the API call in a background task
        object : Task.Backgroundable(project, notifTitle, true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                indicator.text = indicatorTitle

                try {
                    val jsonResponse = runBlocking {
                        if (model != null && apiKey != null) {
                            JSONGenerator.generateJsonWithGemini(apiKey, dataClass, model)
                        } else {
                            JSONGenerator.generateSampleJson(project, dataClass)
                        }
                    }.formatJson()

                    // Switch to the UI thread to update the clipboard and show notification
                    ApplicationManager.getApplication().invokeLater {
                        CopyPasteManager.getInstance().setContents(StringSelection(jsonResponse))
                        showNotification(
                            project,
                            "JSON copied to clipboard!",
                            NotificationType.INFORMATION
                        )
                    }
                } catch (e: Exception) {
                    // Handle exceptions (e.g., network error, invalid API key)
                    ApplicationManager.getApplication().invokeLater {
                        showNotification(
                            project,
                            "Error generating JSON: ${e.message}",
                            NotificationType.ERROR
                        )
                    }
                }
            }
        }.queue()
    }

    // Ensure the action is only visible when the cursor is in a file
    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = e.project != null && editor != null
    }
}
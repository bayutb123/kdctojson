@file:Suppress("UnstableApiUsage")

package com.github.bayutb123.kdctojson

import com.github.bayutb123.kdctojson.services.GeminiCredentialManager
import com.github.bayutb123.kdctojson.utils.*
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

        val dataClass = findDataClassAtCursor(psiFile, editor) ?: run {
            NotificationUtils.showWarning(project, "Please place the cursor inside a Kotlin data class.")
            return
        }

        val model = GeminiUtils.promptModelSelection(project)
        var apiKey = model?.let { GeminiCredentialManager.getInstance().getApiKey() }
        if (apiKey.isNullOrBlank()) {
            apiKey = if (model != null) {
                if (model == UNDEFINED) {
                    return
                } else {
                    GeminiCredentialManager.getInstance().promptAndSaveApiKey()
                }
            } else null
        }

        generateAndCopyJson(project, dataClass, model, apiKey)
    }

    // Ensure the action is only visible when the cursor is in a file
    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = e.project != null && editor != null
    }

    private fun findDataClassAtCursor(psiFile: com.intellij.psi.PsiFile, editor: com.intellij.openapi.editor.Editor): KtClass? {
        val offset = editor.caretModel.offset
        val element = psiFile.findElementAt(offset)
        val dataClass = PsiTreeUtil.getParentOfType(element, KtClass::class.java, false)
        return if (dataClass?.isData() == true) dataClass else null
    }

    private fun generateAndCopyJson(project: com.intellij.openapi.project.Project, dataClass: KtClass, model: String?, apiKey: String?) {
        val notifTitle = if (model != null) "Generating JSON with Gemini" else "Generating JSON"
        val indicatorTitle = if (model != null) "Asking Gemini for a JSON sample..." else "Generating sample JSON..."
        
        // Extract data class text on UI thread (read access required)
        val dataClassText = dataClass.text
        
        object : Task.Backgroundable(project, notifTitle, true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                indicator.text = indicatorTitle

                try {
                    val jsonResponse = runBlocking {
                        if (model != null && apiKey != null) {
                            JSONGenerator.generateJsonWithGemini(apiKey, dataClassText, model)
                        } else {
                            JSONGenerator.generateSampleJson(project, dataClass)
                        }
                    }.formatJson()

                    ApplicationManager.getApplication().invokeLater {
                        CopyPasteManager.getInstance().setContents(StringSelection(jsonResponse))
                        NotificationUtils.showInfo(project, "JSON copied to clipboard!")
                    }
                } catch (e: Exception) {
                    ApplicationManager.getApplication().invokeLater {
                        NotificationUtils.showError(project, "Error generating JSON: ${e.message}")
                    }
                }
            }
        }.queue()
    }
}

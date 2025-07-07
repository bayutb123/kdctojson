@file:Suppress("UnstableApiUsage")

package com.github.bayutb123.kdctojson

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
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
import java.awt.datatransfer.StringSelection

class GetJsonAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return

        val offset = editor.caretModel.offset
        val element = psiFile.findElementAt(offset)
        val type = KtClass::class.java
        val dataClass = PsiTreeUtil.getParentOfType(element, false, type)

        if (dataClass?.isData() == true) {
            val json = generateSampleJson(project, dataClass)
            CopyPasteManager.getInstance().setContents(StringSelection(json))
        }
    }

    private fun generateSampleJson(
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
        val typeIndent = " ".repeat(4 * indentLevel)
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
}

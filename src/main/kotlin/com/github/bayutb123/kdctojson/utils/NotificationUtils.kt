package com.github.bayutb123.kdctojson.utils

import com.github.bayutb123.kdctojson.bundle.KdcToJsonBundle
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object NotificationUtils {
    private val NOTIFICATION_GROUP_ID = KdcToJsonBundle.message("notification.group.id")

    private fun showNotification(
        project: Project,
        content: String,
        type: NotificationType,
        title: String? = null
    ) {
        val notification = NotificationGroupManager.getInstance()
            .getNotificationGroup(NOTIFICATION_GROUP_ID)
            .createNotification(
                title ?: getDefaultTitle(type),
                content,
                type
            )

        notification.notify(project)
    }

    fun showInfo(project: Project, content: String, title: String = KdcToJsonBundle.message("notification.success.title")) {
        showNotification(project, content, NotificationType.INFORMATION, title)
    }

    fun showWarning(project: Project, content: String, title: String = KdcToJsonBundle.message("notification.warning.title")) {
        showNotification(project, content, NotificationType.WARNING, title)
    }

    fun showError(project: Project, content: String, title: String = KdcToJsonBundle.message("notification.error.title")) {
        showNotification(project, content, NotificationType.ERROR, title)
    }

    private fun getDefaultTitle(type: NotificationType): String = when (type) {
        NotificationType.INFORMATION -> KdcToJsonBundle.message("notification.default.title")
        NotificationType.WARNING -> KdcToJsonBundle.message("notification.default.warning.title")
        NotificationType.ERROR -> KdcToJsonBundle.message("notification.default.error.title")
        else -> KdcToJsonBundle.message("notification.default.title")
    }
}
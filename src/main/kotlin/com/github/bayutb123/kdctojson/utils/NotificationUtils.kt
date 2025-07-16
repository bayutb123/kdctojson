package com.github.bayutb123.kdctojson.utils

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object NotificationUtils {
    private const val NOTIFICATION_GROUP_ID = "kdctojson"
    
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
    
    fun showInfo(project: Project, content: String, title: String = "Success") {
        showNotification(project, content, NotificationType.INFORMATION, title)
    }
    
    fun showWarning(project: Project, content: String, title: String = "Warning") {
        showNotification(project, content, NotificationType.WARNING, title)
    }
    
    fun showError(project: Project, content: String, title: String = "Error") {
        showNotification(project, content, NotificationType.ERROR, title)
    }
    
    private fun getDefaultTitle(type: NotificationType): String = when (type) {
        NotificationType.INFORMATION -> "KDC to JSON"
        NotificationType.WARNING -> "KDC to JSON - Warning"
        NotificationType.ERROR -> "KDC to JSON - Error"
        else -> "KDC to JSON"
    }
}

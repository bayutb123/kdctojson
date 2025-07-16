package com.github.bayutb123.kdctojson.utils

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

fun showNotification(project: Project, content: String, type: NotificationType) {
    NotificationGroupManager.getInstance()
        .getNotificationGroup("kdctojson")
        .createNotification(content, type)
        .notify(project)
}
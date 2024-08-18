package com.github.fafeichter.eocs.commit_assistant.ui.notifications

import com.github.fafeichter.eocs.commit_assistant.PluginMessageBundle
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull

object ErrorNotification {
    fun show(@NotNull @NonNls message: String) {
        val notification = Notification(
            "eocs-commit-assistant-errors",
            PluginMessageBundle.getMessage("name"),
            message,
            NotificationType.ERROR
        )

        Notifications.Bus.notify(notification)
    }
}
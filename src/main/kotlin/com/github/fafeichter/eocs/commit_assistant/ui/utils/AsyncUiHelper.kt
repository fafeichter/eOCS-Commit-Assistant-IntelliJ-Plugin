package com.github.fafeichter.eocs.commit_assistant.ui.utils

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project

class AsyncUiHelper {

    companion object {
        fun invokeWriteCommandAsync(project: Project, runnable: () -> Unit) {
            ApplicationManager.getApplication().invokeLater {
                WriteCommandAction.runWriteCommandAction(project, runnable)
            }
        }
    }
}
package com.github.fafeichter.eocs.commit_assistant.ui.commit

import com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator.CommitMessageProvider
import com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator.JiraConnectionConfig
import com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator.eceptions.DomainException
import com.github.fafeichter.eocs.commit_assistant.application.settings.AppSettingsState.Companion.instance
import com.github.fafeichter.eocs.commit_assistant.ui.notifications.ErrorNotification
import com.github.fafeichter.eocs.commit_assistant.ui.utils.AsyncUiHelper
import com.github.fafeichter.eocs.commit_assistant.ui.utils.StringHelper
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory
import com.intellij.ui.EditorTextField

class CommitMessageHandlerFactory : CheckinHandlerFactory() {
    override fun createHandler(panel: CheckinProjectPanel, context: CommitContext): CheckinHandler {
        return object : CheckinHandler() {

            init {
                val project = panel.project;

                val enableInProjects = instance.projects?.enableInProjects
                if (enableInProjects?.isEmpty() != false || StringHelper.splitAndTrim(enableInProjects)
                        .contains(project.name)
                ) {
                    val commitMessageTextField = getCommitMessageTextField(panel)

                    if (commitMessageTextField != null) {
                        populateCommitMessageInputField(project, commitMessageTextField)
                        addCommitMessageTextFieldListener(commitMessageTextField, project)
                    }
                }
            }

            private fun getCommitMessageTextField(panel: CheckinProjectPanel): EditorTextField? {
                return panel.preferredFocusedComponent as? EditorTextField
            }

            private fun addCommitMessageTextFieldListener(commitMessageField: EditorTextField?, project: Project) {
                commitMessageField?.document?.addDocumentListener(object : DocumentListener {

                    override fun documentChanged(event: DocumentEvent) {
                        if (commitMessageField.text == " ") {
                            populateCommitMessageInputField(project, commitMessageField)
                        }
                    }
                })
            }

            private fun populateCommitMessageInputField(project: Project, commitMessageTextField: EditorTextField) {
                try {
                    val jiraIntegration = instance.jiraIntegration
                    val commitMessage = CommitMessageProvider.getCommitMessage(
                        project,
                        JiraConnectionConfig(jiraIntegration!!.installationUrl, jiraIntegration.personalAccessToken)
                    )
                    AsyncUiHelper.invokeWriteCommandAsync(project, { commitMessageTextField.text = commitMessage; })
                } catch (e: DomainException) {
                    val message = e.message ?: e.toString()
                    ErrorNotification.show(message);
                }
            }
        }
    }
}
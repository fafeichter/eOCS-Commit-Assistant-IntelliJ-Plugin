package com.github.fafeichter.eocs.commit_assistant.ui.settings

import com.intellij.ui.HyperlinkLabel
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBEmptyBorder
import java.awt.FlowLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {
    val mainPanel: JPanel

    private val enableInProjectsTextField = JBTextField()
    private val jiraInstallationUrlTextField = JBTextField()
    private val jiraPersonalAccessTokenTextField = JBPasswordField()

    init {
        val inputFieldsSpacing = JBEmptyBorder(0, 20, 0, 0)

        val projectsGroupTitle = TitledSeparator(SettingsMessageBundle.getMessage("settings.projects"));
        val enableInProjectsTitle =
            JBLabel(SettingsMessageBundle.getMessage("settings.projects.enable-in") + ": ")
        enableInProjectsTextField.emptyText.text = SettingsMessageBundle.getMessage(
            "settings.projects.enable-in.placeholder"
        )

        val jiraGroupTitle = TitledSeparator(SettingsMessageBundle.getMessage("settings.jira"));
        val jiraInstallationUrlLabel =
            JBLabel(SettingsMessageBundle.getMessage("settings.jira.installation-url") + ": ")
        val jiraPersonalAccessTokenLabel = JBLabel(
            SettingsMessageBundle.getMessage("settings.jira.personal-access-token") + ": "
        )
        val jiraGeneratePersonalAccessTokenLink = HyperlinkLabel(
            SettingsMessageBundle.getMessage("settings.jira.personal-access-token.generate")
        )
        val generatePersonalAccessTokenLinkPanel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0)).apply {
            add(jiraGeneratePersonalAccessTokenLink)
            isVisible = false
        }
        initJiraInstallationUrlTextField(jiraGeneratePersonalAccessTokenLink, generatePersonalAccessTokenLinkPanel)

        mainPanel = FormBuilder.createFormBuilder()
            .addComponent(projectsGroupTitle)
            .addLabeledComponent(
                inputFieldsSpacing.wrap(enableInProjectsTitle),
                enableInProjectsTextField,
                5,
                false
            )
            .addComponent(jiraGroupTitle)
            .addLabeledComponent(
                inputFieldsSpacing.wrap(jiraInstallationUrlLabel),
                jiraInstallationUrlTextField,
                5,
                false
            )
            .addLabeledComponent(
                inputFieldsSpacing.wrap(jiraPersonalAccessTokenLabel),
                jiraPersonalAccessTokenTextField,
                5,
                false
            )
            .addComponentToRightColumn(generatePersonalAccessTokenLinkPanel)
            .addComponentFillVertically(JPanel(), 0)
            .panel

    }

    val preferredFocusedComponent: JComponent
        get() = enableInProjectsTextField

    fun getEnableInProjects(): String {
        return enableInProjectsTextField.getText()
    }

    fun setEnableInProjects(enableInProjects: String?) {
        enableInProjectsTextField.setText(enableInProjects)
    }

    fun getJiraInstallationUrl(): String {
        return jiraInstallationUrlTextField.getText()
    }

    fun setJiraInstallationUrl(jiraInstallationUrl: String?) {
        jiraInstallationUrlTextField.setText(jiraInstallationUrl)
    }

    fun getJiraPersonalAccessToken(): String {
        return String(jiraPersonalAccessTokenTextField.getPassword())
    }

    fun setJiraPersonalAccessToken(jiraPersonalAccessToken: String?) {
        jiraPersonalAccessTokenTextField.setText(jiraPersonalAccessToken)
    }

    private fun initJiraInstallationUrlTextField(
        generatePersonalAccessTokenLink: HyperlinkLabel,
        generatePersonalAccessTokenLinkPanel: JPanel
    ) {
        jiraInstallationUrlTextField.emptyText.text = "https://jira.yourcompany.com"
        jiraInstallationUrlTextField.document.addDocumentListener(object : DocumentListener {

            override fun insertUpdate(e: DocumentEvent?) {
                onChange()
            }

            override fun removeUpdate(e: DocumentEvent?) {
                onChange()
            }

            override fun changedUpdate(e: DocumentEvent?) {
                onChange()
            }

            private fun onChange() =
                if (jiraInstallationUrlTextField.text.isNotEmpty()) {
                    generatePersonalAccessTokenLink.setHyperlinkTarget(
                        jiraInstallationUrlTextField.text + "/secure/ViewProfile.jspa"
                    )
                    generatePersonalAccessTokenLinkPanel.isVisible = true
                } else {
                    generatePersonalAccessTokenLinkPanel.isVisible = false
                }
        })
    }
}
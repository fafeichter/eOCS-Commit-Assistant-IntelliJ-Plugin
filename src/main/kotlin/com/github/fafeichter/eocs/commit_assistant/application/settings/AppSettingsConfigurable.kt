package com.github.fafeichter.eocs.commit_assistant.application.settings

import com.github.fafeichter.eocs.commit_assistant.PluginMessageBundle
import com.github.fafeichter.eocs.commit_assistant.ui.settings.AppSettingsComponent
import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * Provides controller functionality for application settings.
 */
class AppSettingsConfigurable : Configurable {
    private var settingsComponent: AppSettingsComponent? = null

    // A default constructor with no arguments is required because this implementation
    // is registered in an applicationConfigurable EP
    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return PluginMessageBundle.getMessage("settings")
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return settingsComponent!!.preferredFocusedComponent
    }

    override fun createComponent(): JComponent {
        settingsComponent = AppSettingsComponent()
        return settingsComponent!!.mainPanel
    }

    override fun isModified(): Boolean {
        val settings = AppSettingsState.instance
        return settingsComponent!!.getEnableInProjects() != settings.projects!!.enableInProjects
                || settingsComponent!!.getJiraInstallationUrl() != settings.jiraIntegration!!.installationUrl
                || settingsComponent!!.getJiraPersonalAccessToken() != settings.jiraIntegration!!.personalAccessToken
    }

    override fun apply() {
        val settings = AppSettingsState.instance

        val projects = settings.projects
        projects!!.enableInProjects = settingsComponent!!.getEnableInProjects()

        val jiraIntegration = settings.jiraIntegration
        jiraIntegration!!.installationUrl = settingsComponent!!.getJiraInstallationUrl()
        jiraIntegration.personalAccessToken = settingsComponent!!.getJiraPersonalAccessToken()
    }

    override fun reset() {
        val settings = AppSettingsState.instance
        settingsComponent!!.setEnableInProjects(settings.projects!!.enableInProjects)
        settingsComponent!!.setJiraInstallationUrl(settings.jiraIntegration!!.installationUrl)
        settingsComponent!!.setJiraPersonalAccessToken(settings.jiraIntegration!!.personalAccessToken)
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}
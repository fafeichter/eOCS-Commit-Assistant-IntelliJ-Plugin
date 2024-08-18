package com.github.fafeichter.eocs.commit_assistant.application.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Stores the application settings in a persistent way.
 */
@State(
    name = "org.intellij.sdk.settings.AppSettingsState",
    storages = [Storage("eOCSCommitAssistantPluginSettings.xml")]
)
class AppSettingsState : PersistentStateComponent<AppSettingsState?> {

    var projects: Projects? = Projects()
    var jiraIntegration: JiraIntegration? = JiraIntegration()

    override fun getState(): AppSettingsState {
        return this
    }

    override fun loadState(state: AppSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: AppSettingsState
            get() = ApplicationManager.getApplication().getService(AppSettingsState::class.java)
    }

    class JiraIntegration(
        var installationUrl: String? = "", var personalAccessToken: String? = ""
    ) {

    }

    class Projects(
        var enableInProjects: String? = ""
    ) {

    }
}
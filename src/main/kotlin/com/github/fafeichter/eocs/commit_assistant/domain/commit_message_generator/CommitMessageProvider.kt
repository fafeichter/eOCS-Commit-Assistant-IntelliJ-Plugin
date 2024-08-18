package com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator

import com.atlassian.jira.rest.client.api.RestClientException
import com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator.eceptions.DomainException
import com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator.i18n.DomainErrorsMessageBundle
import com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator.utils.GitHelper
import com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator.jira.JiraClient
import com.intellij.openapi.project.Project

class CommitMessageProvider {

    companion object {
        fun getCommitMessage(project: Project, jiraConnectionConfig: JiraConnectionConfig): String {
            val jiraInstallationUrl = jiraConnectionConfig.url
            val jiraAccessToken = jiraConnectionConfig.token

            if (jiraInstallationUrl!!.isEmpty() || jiraAccessToken!!.isEmpty()) {
                throw DomainException(
                    DomainErrorsMessageBundle.getMessage(
                        "errors.jira-integration.settings.missing"
                    )
                );
            }

            val jiraClient = JiraClient(jiraInstallationUrl, jiraAccessToken)
            val gitBranchName = GitHelper.getCurrentBranchName(project)

            if (gitBranchName != null) {
                val jiraIssueId = GitBranchParser.parseJiraIssueIdFromGitBranchName(gitBranchName)
                if (jiraIssueId != null) {
                    try {
                        val issue = jiraClient.getIssue(jiraIssueId)
                        val commitMessage = CommitMessage(issue)
                        return commitMessage.toString();
                    } catch (e: RestClientException) {
                        if (e.statusCode.orNull() == 401) {
                            throw DomainException(
                                DomainErrorsMessageBundle.getMessage("errors.jira-issue.lookup.401"), e
                            );
                        } else if (e.statusCode.orNull() == 403) {
                            throw DomainException(
                                DomainErrorsMessageBundle.getMessage(
                                    "errors.jira-issue.lookup.403",
                                    jiraIssueId
                                ), e
                            );
                        } else if (e.statusCode.orNull() == 404) {
                            throw DomainException(
                                DomainErrorsMessageBundle.getMessage(
                                    "errors.jira-issue.lookup.404", jiraIssueId
                                ), e
                            );
                        } else {
                            throw DomainException(
                                DomainErrorsMessageBundle.getMessage(
                                    "errors.jira-issue.lookup.unknown",
                                    e.message
                                ), e
                            );
                        }
                    }
                } else {
                    throw DomainException(
                        DomainErrorsMessageBundle.getMessage(
                            "errors.git-branch.parse.jira-issue-id.missing",
                            gitBranchName
                        )
                    );
                }
            } else {
                throw DomainException(
                    DomainErrorsMessageBundle.getMessage("errors.git-branch.missing")
                );
            }
        }
    }
}
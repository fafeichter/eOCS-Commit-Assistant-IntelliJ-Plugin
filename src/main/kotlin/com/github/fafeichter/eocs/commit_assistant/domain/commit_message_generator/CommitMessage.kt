package com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator

import com.atlassian.jira.rest.client.api.domain.Issue

class CommitMessage(issue: Issue?) {
    private var jiraIssueSummary: String = issue?.summary ?: ""
    private var jiraIssueId: String = issue?.key ?: ""

    override fun toString(): String {
        return "$jiraIssueId $jiraIssueSummary"
    }
}

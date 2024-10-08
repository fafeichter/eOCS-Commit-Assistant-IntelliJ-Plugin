package com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator.jira

import com.atlassian.jira.rest.client.api.JiraRestClient
import com.atlassian.jira.rest.client.api.JiraRestClientFactory
import com.atlassian.jira.rest.client.api.domain.Issue
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory
import java.net.URI

class JiraClient(private val url: String, private val token: String) {
    private val restClient: JiraRestClient

    init {
        restClient = jiraRestClient
    }

    private val jiraRestClient: JiraRestClient
        get() {
            val handler = BearerHttpAuthenticationHandler(token)
            val factory: JiraRestClientFactory = AsynchronousJiraRestClientFactory()
            return factory.create(jiraUri, handler)
        }

    private val jiraUri: URI
        get() = URI.create(url)

    fun getIssue(issueKey: String?): Issue {
        return restClient.issueClient
            .getIssue(issueKey)
            .claim()
    }
}
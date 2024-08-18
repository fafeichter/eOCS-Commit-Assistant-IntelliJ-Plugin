package com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator

class GitBranchParser {

    companion object {
        private const val JIRA_ISSUE_ID_REGEX = "[A-Z]+-\\d+"

        fun parseJiraIssueIdFromGitBranchName(gitBranchName: String): String? {
            return Regex(JIRA_ISSUE_ID_REGEX)
                .find(gitBranchName)
                ?.value
        }
    }
}
package com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator.utils

import com.intellij.openapi.project.Project
import git4idea.GitUtil

class GitHelper {

    companion object {
        fun getCurrentBranchName(project: Project): String? {
            val repositoryManager = GitUtil.getRepositoryManager(project)
            return repositoryManager.repositories[0].currentBranch?.name
        }
    }
}
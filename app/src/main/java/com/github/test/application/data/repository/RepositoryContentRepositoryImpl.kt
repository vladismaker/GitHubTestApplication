package com.github.test.application.data.repository

import com.github.test.application.data.api.GitHubApi
import com.github.test.application.data.api.RetroFitInstance
import com.github.test.application.domain.dataclasses.DataRepositoryContent
import com.github.test.application.domain.dataclasses.SealedResponse
import com.github.test.application.domain.repository.RepositoryContentRepository

class RepositoryContentRepositoryImpl: RepositoryContentRepository {
    override suspend fun getRepositoryContent(owner: String, repo: String, path: String): SealedResponse<DataRepositoryContent> {
        return try {
            val repositoryContentCalls = RetroFitInstance.getInstance().create(GitHubApi::class.java)
            val response = repositoryContentCalls.getRepositoryContents(owner, repo, path)

            val parentPath = if (path.isNotEmpty()) {
                path.substringBeforeLast('/')
            } else {
                null
            }
            SealedResponse.Success(DataRepositoryContent(response, parentPath))
        } catch (e: Exception) {
            SealedResponse.ErrorMessage("Error: ${e.message}")
        }
    }
}
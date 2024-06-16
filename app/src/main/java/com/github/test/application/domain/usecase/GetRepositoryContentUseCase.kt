package com.github.test.application.domain.usecase

import com.github.test.application.domain.dataclasses.DataRepositoryContent
import com.github.test.application.domain.dataclasses.SealedResponse
import com.github.test.application.domain.repository.RepositoryContentRepository

class GetRepositoryContentUseCase(private val repositoryContentRepository: RepositoryContentRepository) {
    suspend fun execute(owner: String, repo: String, path: String): SealedResponse<DataRepositoryContent> {
        return repositoryContentRepository.getRepositoryContent(owner, repo, path)
    }
}
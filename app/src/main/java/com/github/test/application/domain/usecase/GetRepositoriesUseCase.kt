package com.github.test.application.domain.usecase

import com.github.test.application.domain.dataclasses.DataRepository
import com.github.test.application.domain.dataclasses.SealedResponse
import com.github.test.application.domain.repository.SearchRepositoryRepository

class GetRepositoriesUseCase(private val searchRepositoryRepository: SearchRepositoryRepository) {
    suspend fun execute(query: String): SealedResponse<List<DataRepository>> {
        return searchRepositoryRepository.searchRepositories(query)
    }
}
package com.github.test.application.data.repository

import com.github.test.application.data.api.GitHubApi
import com.github.test.application.data.api.RetroFitInstance
import com.github.test.application.domain.dataclasses.DataRepository
import com.github.test.application.domain.dataclasses.SealedResponse
import com.github.test.application.domain.repository.SearchRepositoryRepository

class SearchRepositoryRepositoryImpl:SearchRepositoryRepository {
    override suspend fun searchRepositories(query: String): SealedResponse<List<DataRepository>> {
        return try {
            val repositoriesCalls = RetroFitInstance.getInstance().create(GitHubApi::class.java)
            val response = repositoriesCalls.searchRepositories(query)
            SealedResponse.Success(response.items)
        } catch (e: Exception) {
            SealedResponse.ErrorMessage("Error: ${e.message}")
        }
    }
}
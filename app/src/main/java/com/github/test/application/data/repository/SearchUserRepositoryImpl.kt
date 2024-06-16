package com.github.test.application.data.repository

import com.github.test.application.data.api.GitHubApi
import com.github.test.application.data.api.RetroFitInstance
import com.github.test.application.domain.dataclasses.DataUser
import com.github.test.application.domain.dataclasses.SealedResponse
import com.github.test.application.domain.repository.SearchUserRepository

class SearchUserRepositoryImpl:SearchUserRepository {
    override suspend fun searchUsers(query:String): SealedResponse<List<DataUser>> {
        return try {
            val usersCalls = RetroFitInstance.getInstance().create(GitHubApi::class.java)
            val response = usersCalls.searchUsers(query)
            SealedResponse.Success(response.items)
        } catch (e: Exception) {
            SealedResponse.ErrorMessage("Error: ${e.message}")
        }
    }
}
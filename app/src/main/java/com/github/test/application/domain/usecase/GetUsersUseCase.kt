package com.github.test.application.domain.usecase

import com.github.test.application.domain.dataclasses.DataUser
import com.github.test.application.domain.dataclasses.SealedResponse
import com.github.test.application.domain.repository.SearchUserRepository

class GetUsersUseCase(private val searchUserRepository:SearchUserRepository) {
    suspend fun execute(query: String): SealedResponse<List<DataUser>>  {
        return searchUserRepository.searchUsers(query)
    }
}
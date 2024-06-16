package com.github.test.application.domain.repository

import com.github.test.application.domain.dataclasses.DataRepository
import com.github.test.application.domain.dataclasses.SealedResponse

interface SearchRepositoryRepository {
    suspend fun searchRepositories(query:String): SealedResponse<List<DataRepository>>
}
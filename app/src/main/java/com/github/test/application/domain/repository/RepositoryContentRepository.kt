package com.github.test.application.domain.repository

import com.github.test.application.domain.dataclasses.DataRepositoryContent
import com.github.test.application.domain.dataclasses.SealedResponse

interface RepositoryContentRepository {
    suspend fun getRepositoryContent(owner: String, repo: String, path: String): SealedResponse<DataRepositoryContent>
}
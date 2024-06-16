package com.github.test.application.domain.repository

import com.github.test.application.domain.dataclasses.DataUser
import com.github.test.application.domain.dataclasses.SealedResponse

interface SearchUserRepository {
    suspend fun searchUsers(query:String): SealedResponse<List<DataUser>>
}
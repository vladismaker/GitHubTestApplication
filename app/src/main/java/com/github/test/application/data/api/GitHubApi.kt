package com.github.test.application.data.api

import com.github.test.application.domain.dataclasses.DataRepositorySearchResponse
import com.github.test.application.domain.dataclasses.DataUserSearchResponse
import com.github.test.application.domain.dataclasses.DataRepositoryContentEntry
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): DataUserSearchResponse

    @GET("search/repositories")
    suspend fun searchRepositories(@Query("q") query: String): DataRepositorySearchResponse

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getRepositoryContents(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String = ""
    ): List<DataRepositoryContentEntry>
}
package com.github.test.application.domain.dataclasses

sealed class SearchResult {
    data class UserResult(val user: DataUser) : SearchResult()
    data class RepoResult(val repo: DataRepository) : SearchResult()
}

package com.github.test.application.domain.usecase

import com.github.test.application.domain.dataclasses.DataRepository
import com.github.test.application.domain.dataclasses.DataUser
import com.github.test.application.domain.dataclasses.SealedResponse
import com.github.test.application.domain.dataclasses.SearchResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CombineUsersAndRepositoriesUseCase(
    private val getUsersUseCase: GetUsersUseCase,
    private val getRepositoriesUseCase: GetRepositoriesUseCase
) {
    suspend fun execute(query: String): SealedResponse<List<SearchResult>> = coroutineScope {
        val usersDeferred = async { getUsersUseCase.execute(query) }
        val repositoriesDeferred = async { getRepositoriesUseCase.execute(query) }

        val usersResult = usersDeferred.await()
        val repositoriesResult = repositoriesDeferred.await()

        return@coroutineScope handleResults(usersResult, repositoriesResult)
    }

    private fun handleResults(
        usersResult: SealedResponse<List<DataUser>>,
        repositoriesResult: SealedResponse<List<DataRepository>>
    ): SealedResponse<List<SearchResult>> {
        return when {
            usersResult is SealedResponse.Success && repositoriesResult is SealedResponse.Success -> {
                val combinedResults = mutableListOf<SearchResult>().apply {
                    addAll(usersResult.data.map { SearchResult.UserResult(it) })
                    addAll(repositoriesResult.data.map { SearchResult.RepoResult(it) })
                }
                SealedResponse.Success(combinedResults.sortedWith(compareBy {
                    when (it) {
                        is SearchResult.UserResult -> it.user.login
                        is SearchResult.RepoResult -> it.repo.name
                    }
                }))
            }
            usersResult is SealedResponse.ErrorMessage && repositoriesResult is SealedResponse.ErrorMessage -> {
                val combinedErrorMessage = "${usersResult.errorMessage} & ${repositoriesResult.errorMessage}"
                SealedResponse.ErrorMessage(combinedErrorMessage)
            }
            usersResult is SealedResponse.ErrorMessage -> {
                SealedResponse.ErrorMessage(usersResult.errorMessage)
            }
            repositoriesResult is SealedResponse.ErrorMessage -> {
                SealedResponse.ErrorMessage(repositoriesResult.errorMessage)
            }
            else -> {
                SealedResponse.ErrorMessage("Unknown error")
            }
        }
    }
}
package com.github.test.application.domain.dataclasses

data class DataRepositorySearchResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<DataRepository>
)

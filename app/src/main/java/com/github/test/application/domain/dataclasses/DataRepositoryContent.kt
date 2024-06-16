package com.github.test.application.domain.dataclasses

data class DataRepositoryContent(
    val entries: List<DataRepositoryContentEntry>,
    val parentPath: String?
)
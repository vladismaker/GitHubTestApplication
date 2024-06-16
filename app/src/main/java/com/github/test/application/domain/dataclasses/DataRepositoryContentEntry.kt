package com.github.test.application.domain.dataclasses

data class DataRepositoryContentEntry(
    val name: String,
    val path: String,
    val type: String,
    val url: String,
    val html_url: String
)

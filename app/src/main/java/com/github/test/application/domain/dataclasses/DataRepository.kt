package com.github.test.application.domain.dataclasses

data class DataRepository(
    val name: String,
    val id: Int,
    val forks_count: String,
    val owner: DataUser,
    val description: String
)

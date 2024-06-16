package com.github.test.application.di

import com.github.test.application.domain.repository.RepositoryContentRepository
import com.github.test.application.domain.repository.SearchRepositoryRepository
import com.github.test.application.domain.repository.SearchUserRepository
import com.github.test.application.domain.repository.WebViewRepository
import com.github.test.application.domain.usecase.CombineUsersAndRepositoriesUseCase
import com.github.test.application.domain.usecase.GetInternetStatusUseCase
import com.github.test.application.domain.usecase.GetRepositoriesUseCase
import com.github.test.application.domain.usecase.GetRepositoryContentUseCase
import com.github.test.application.domain.usecase.GetUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModel {

    @Provides
    fun provideGetInternetStatusUseCase(webViewRepository: WebViewRepository):GetInternetStatusUseCase{
        return GetInternetStatusUseCase(webViewRepository = webViewRepository)
    }

    @Provides
    fun provideGetRepositoryContentUseCase(repositoryContentRepository: RepositoryContentRepository):GetRepositoryContentUseCase{
        return GetRepositoryContentUseCase(repositoryContentRepository = repositoryContentRepository)
    }

    @Provides
    fun provideGetUsersUseCase(searchUserRepository: SearchUserRepository):GetUsersUseCase{
        return GetUsersUseCase(searchUserRepository = searchUserRepository)
    }

    @Provides
    fun provideGetRepositoriesUseCase(searchRepositoryRepository: SearchRepositoryRepository):GetRepositoriesUseCase{
        return GetRepositoriesUseCase(searchRepositoryRepository = searchRepositoryRepository)
    }

    @Provides
    fun provideCombineUsersAndRepositoriesUseCase(getUsersUseCase: GetUsersUseCase, getRepositoriesUseCase:GetRepositoriesUseCase):CombineUsersAndRepositoriesUseCase{
        return CombineUsersAndRepositoriesUseCase(getUsersUseCase = getUsersUseCase, getRepositoriesUseCase = getRepositoriesUseCase)
    }
}


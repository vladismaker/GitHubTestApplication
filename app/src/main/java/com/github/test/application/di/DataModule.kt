package com.github.test.application.di

import android.content.Context
import com.github.test.application.data.repository.RepositoryContentRepositoryImpl
import com.github.test.application.data.repository.SearchRepositoryRepositoryImpl
import com.github.test.application.data.repository.SearchUserRepositoryImpl
import com.github.test.application.data.repository.WebViewRepositoryImpl
import com.github.test.application.domain.repository.RepositoryContentRepository
import com.github.test.application.domain.repository.SearchRepositoryRepository
import com.github.test.application.domain.repository.SearchUserRepository
import com.github.test.application.domain.repository.WebViewRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideWebViewRepository(@ApplicationContext context:Context):WebViewRepository{
        return WebViewRepositoryImpl(context = context)
    }

    @Provides
    @Singleton
    fun provideRepositoryContentRepository():RepositoryContentRepository{
        return RepositoryContentRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideSearchUserRepository(): SearchUserRepository {
        return SearchUserRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideSearchRepositoryRepository(): SearchRepositoryRepository {
        return SearchRepositoryRepositoryImpl()
    }
}
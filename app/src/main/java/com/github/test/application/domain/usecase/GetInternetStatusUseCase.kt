package com.github.test.application.domain.usecase

import com.github.test.application.domain.repository.WebViewRepository

class GetInternetStatusUseCase(private val webViewRepository: WebViewRepository) {
    fun execute():Boolean{
        return webViewRepository.getInternetStatus()
    }
}
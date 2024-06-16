package com.github.test.application.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.test.application.domain.usecase.GetInternetStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val getInternetStatusUseCase:GetInternetStatusUseCase
):ViewModel() {

    private val _isInternet = MutableLiveData<Boolean>()
    val isInternet: LiveData<Boolean> get() = _isInternet

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun checkInternet(){
        viewModelScope.launch {
            try {
                val internetStatus = async { getInternetStatusUseCase.execute() }.await()
                _isInternet.postValue(internetStatus)
            }catch (error:Throwable){
                _errorMessage.postValue(error.message)
            }
        }
    }
}
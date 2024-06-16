package com.github.test.application.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.test.application.domain.dataclasses.SealedResponse
import com.github.test.application.domain.dataclasses.SearchResult
import com.github.test.application.domain.usecase.CombineUsersAndRepositoriesUseCase
import com.github.test.application.domain.usecase.GetInternetStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUsersAndRepositoriesViewModel @Inject constructor(
    private val getInternetStatusUseCase: GetInternetStatusUseCase,
    private val combineUsersAndRepositoriesUseCase: CombineUsersAndRepositoriesUseCase
):ViewModel() {
    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> get() = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isInternet = MutableLiveData<Boolean>()
    val isInternet: LiveData<Boolean> get() = _isInternet

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

    fun performSearch(query: String) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                when(val results = combineUsersAndRepositoriesUseCase.execute(query)){
                    is SealedResponse.Success -> {
                        val repositoryContent = results.data
                        _searchResults.postValue(repositoryContent)
                    }
                    is SealedResponse.ErrorMessage -> {
                        val errorMessage  =  "Error: ${results.errorMessage}"
                        _errorMessage.value = errorMessage
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.postValue(false)
            }
        }

    }

    fun clearList(){
        _searchResults.value = listOf()
    }
}
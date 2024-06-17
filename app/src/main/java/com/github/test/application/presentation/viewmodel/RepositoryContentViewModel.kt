package com.github.test.application.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.test.application.domain.dataclasses.DataRepository
import com.github.test.application.domain.dataclasses.DataRepositoryContentEntry
import com.github.test.application.domain.dataclasses.SealedResponse
import com.github.test.application.domain.usecase.GetInternetStatusUseCase
import com.github.test.application.domain.usecase.GetRepositoryContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryContentViewModel @Inject constructor(
    private val getInternetStatusUseCase: GetInternetStatusUseCase,
    private val getRepositoryContentUseCase: GetRepositoryContentUseCase
) : ViewModel() {
    private val _dataRepository = MutableLiveData<DataRepository>()
    val dataRepository: LiveData<DataRepository> get() = _dataRepository

    private val _listRepositoryContent = MutableLiveData<List<DataRepositoryContentEntry>>()
    val listRepositoryContent: LiveData<List<DataRepositoryContentEntry>> get() = _listRepositoryContent

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

    fun clearList(){
        _listRepositoryContent.value = listOf()
    }

    fun setDataRepositiryToRepositoryContent(repository: DataRepository) {
        _dataRepository.value = repository
    }

    fun getRepositoryContent(owner: String, repo: String, path: String) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                when(val results = getRepositoryContentUseCase.execute(owner, repo, path)){
                    is SealedResponse.Success -> {
                        val repositoryContent = results.data.entries
                        _listRepositoryContent.postValue(repositoryContent.sortedWith(compareBy { it.type != "dir" }))
                    }
                    is SealedResponse.ErrorMessage -> {
                        val errorMessage  =  "Error: ${results.errorMessage}"
                        _errorMessage.postValue(errorMessage)
                    }
                }
            } catch (error: Throwable) {
                _errorMessage.postValue("Error: ${error.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
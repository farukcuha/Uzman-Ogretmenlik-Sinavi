package com.pandorina.uzman_ogretmenlik.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel(){
    var job: Job? = null
    protected abstract val TAG: String

    protected val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    protected val _error = MutableStateFlow<String?>("")
    val error: StateFlow<String?> = _error

    init {
        observeError()
    }

    private fun observeError(){
        launchViewModelScope {
            error.collectLatest {
                Log.d(TAG, "Error : $it")
            }
        }
    }

    private fun initLoading(){
        _isLoading.value = true
    }

    protected fun stopLoading(){
        _isLoading.value = false
    }

    fun sendError(errorMessage: String?){
        _error.value = errorMessage
    }

    fun launchViewModelScope(process: suspend () -> Unit){
        viewModelScope.launch {
            initLoading()
            process()
        }
    }

    override fun onCleared() {
        super.onCleared()
        job = null
    }
}
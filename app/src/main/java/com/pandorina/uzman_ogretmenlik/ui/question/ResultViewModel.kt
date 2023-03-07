package com.pandorina.uzman_ogretmenlik.ui.question

import com.pandorina.uzman_ogretmenlik.core.BaseViewModel
import com.pandorina.uzman_ogretmenlik.domain.StatisticRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    val repository: StatisticRepository): BaseViewModel() {

    override val TAG: String = "Result"

    fun insertExamResult(title: String, correctCount: Int, wrongCount: Int){
        launchViewModelScope {
            try {
                repository.insertExamResult(title, correctCount, wrongCount)
                stopLoading()
            } catch (e: Exception){
                sendError(e.localizedMessage)
                stopLoading()
            }
        }
    }
}
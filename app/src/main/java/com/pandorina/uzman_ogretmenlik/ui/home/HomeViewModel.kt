package com.pandorina.uzman_ogretmenlik.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.pandorina.uzman_ogretmenlik.core.BaseViewModel
import com.pandorina.uzman_ogretmenlik.domain.ExamRepository
import com.pandorina.uzman_ogretmenlik.domain.StatisticRepository
import com.pandorina.uzman_ogretmenlik.domain.model.QuestionsResponse
import com.pandorina.uzman_ogretmenlik.ui.SettingsFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ExamRepository,
    private val statisticRepository: StatisticRepository
): BaseViewModel() {
    override val TAG: String = "Home"

    val correctCount = MutableLiveData(0)
    val wrongCount = MutableLiveData(0)
    val examTime = MutableLiveData<Long?>()
    val randomQuestions = MutableLiveData<List<QuestionsResponse.Question>>()

    init {
        launchViewModelScope {
            repository.getExamTime().collectLatest { result ->
                result.onSuccess {
                    examTime.value = it
                    stopLoading()
                }.onFailure { throwable ->
                    sendError(throwable.localizedMessage)
                    stopLoading()
                }
            }
        }
        fetchCorrectCount()
        fetchWrongCount()
    }

    private fun fetchCorrectCount(){
        launchViewModelScope {
            statisticRepository.getTotalCorrectCount().collectLatest { result ->
                correctCount.value = result
            }
        }
    }

    private fun fetchWrongCount(){
        launchViewModelScope {
            statisticRepository.getTotalWrongCount().collectLatest { result ->
                wrongCount.value = result
            }
        }
    }

    fun fetchRandomQuestions(questionCount: Int){
        launchViewModelScope {
            repository.getRandomQuestions(questionCount).collectLatest { result ->
                result.onSuccess {
                    randomQuestions.value = it.questions
                    stopLoading()
                }.onFailure { throwable ->
                    sendError(throwable.localizedMessage)
                    stopLoading()
                }
            }
        }
    }

    fun clearRandomQuestions(){
        randomQuestions.value = emptyList()
    }
}
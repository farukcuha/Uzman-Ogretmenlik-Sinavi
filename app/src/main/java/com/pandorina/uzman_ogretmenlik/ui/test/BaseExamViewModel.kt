package com.pandorina.uzman_ogretmenlik.ui.test

import androidx.lifecycle.MutableLiveData
import com.pandorina.uzman_ogretmenlik.core.BaseViewModel
import com.pandorina.uzman_ogretmenlik.domain.ExamRepository
import com.pandorina.uzman_ogretmenlik.domain.StatisticRepository
import com.pandorina.uzman_ogretmenlik.domain.model.QuestionsResponse
import com.pandorina.uzman_ogretmenlik.domain.model.TestsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


abstract class BaseExamViewModel(
    val examRepository: ExamRepository,
    val statisticRepository: StatisticRepository
): BaseViewModel() {

    override val TAG: String
        get() = "BaseExam"

    val testResults = MutableLiveData<List<ExamTestsAdapter.TestWithResult>>()
    val questions = MutableLiveData<List<QuestionsResponse.Question>>()

    abstract fun fetchExamTestsProcess(): Flow<Result<TestsResponse>>?

    abstract fun fetchQuestionsProcess(): Flow<Result<QuestionsResponse>>?

    fun fetchExamTests() {
        launchViewModelScope {
            fetchExamTestsProcess()?.collectLatest { result ->
                result.onSuccess { response ->
                    reorderByResults(response.tests)
                }.onFailure { throwable ->
                    sendError(throwable.localizedMessage)
                    stopLoading()
                }
            }
        }
    }

    private suspend fun reorderByResults(tests: List<TestsResponse.Test>?) {
        statisticRepository.getAllExamResults().collect { results ->
            val list = mutableListOf<ExamTestsAdapter.TestWithResult>()
            tests?.forEach { test ->
                val result = results.find { statistic ->
                    statistic.title == "${test.testTitle} - ${test.testNo}"
                }
                list.add(ExamTestsAdapter.TestWithResult(test, result))
            }
            testResults.value = list.toList()
            stopLoading()
        }
    }

    fun fetchQuestions(){
        launchViewModelScope {
            stopLoading()
            fetchQuestionsProcess()?.collectLatest { result ->
                result.onSuccess {
                    questions.value = it.questions
                }.onFailure { throwable ->
                    sendError(throwable.localizedMessage)
                    stopLoading()
                }
            }
        }
    }

    fun clearQuestions(){
        questions.value = emptyList()
    }
}
package com.pandorina.uzman_ogretmenlik.ui.test

import com.pandorina.uzman_ogretmenlik.domain.ExamRepository
import com.pandorina.uzman_ogretmenlik.domain.StatisticRepository
import com.pandorina.uzman_ogretmenlik.domain.model.QuestionsResponse
import com.pandorina.uzman_ogretmenlik.domain.model.TestsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TrialExamTestsViewModel @Inject constructor(
    examRepository: ExamRepository,
    statisticRepository: StatisticRepository
): BaseExamViewModel(examRepository, statisticRepository) {

    override val TAG: String = "TrialExamTitles"

    var testNo: Int? = null

    override fun fetchExamTestsProcess(): Flow<Result<TestsResponse>> {
        return examRepository.getTrialExamTests()
    }

    override fun fetchQuestionsProcess(): Flow<Result<QuestionsResponse>>? {
        if (testNo == null) return null
        return examRepository.getTrialExamQuestions(testNo!!)
    }
}
package com.pandorina.uzman_ogretmenlik.ui.test

import com.pandorina.uzman_ogretmenlik.domain.ExamRepository
import com.pandorina.uzman_ogretmenlik.domain.StatisticRepository
import com.pandorina.uzman_ogretmenlik.domain.model.QuestionsResponse
import com.pandorina.uzman_ogretmenlik.domain.model.TestsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ExamTestsViewModel @Inject constructor(
    examRepository: ExamRepository,
    statisticRepository: StatisticRepository
): BaseExamViewModel(examRepository, statisticRepository) {

    override val TAG: String = "ExamTests"

    var title: String? = null
    var testNo: Int? = null

    override fun fetchExamTestsProcess(): Flow<Result<TestsResponse>>? {
        return title?.let {
            examRepository.getStudyExamTests(it)
        }
    }

    override fun fetchQuestionsProcess(): Flow<Result<QuestionsResponse>>? {
        if (title == null) return null
        if (testNo == null) return null
        return examRepository.getStudyExamQuestions(title!!, testNo!!)
    }
}
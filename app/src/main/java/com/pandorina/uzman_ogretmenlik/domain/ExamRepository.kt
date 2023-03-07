package com.pandorina.uzman_ogretmenlik.domain

import com.pandorina.uzman_ogretmenlik.domain.model.QuestionsResponse
import com.pandorina.uzman_ogretmenlik.domain.model.TestsResponse
import com.pandorina.uzman_ogretmenlik.domain.model.TitlesResponse
import kotlinx.coroutines.flow.Flow

interface ExamRepository {

    fun getStudyExamTitles(): Flow<Result<TitlesResponse>>

    fun getStudyExamTests( title: String): Flow<Result<TestsResponse>>

    fun getStudyExamQuestions(title: String, testNo: Int): Flow<Result<QuestionsResponse>>

    fun getTrialExamTests(): Flow<Result<TestsResponse>>

    fun getTrialExamQuestions(testNo: Int): Flow<Result<QuestionsResponse>>

    fun getRandomQuestions(count: Int): Flow<Result<QuestionsResponse>>

    fun getExamTime(): Flow<Result<Long>>
}
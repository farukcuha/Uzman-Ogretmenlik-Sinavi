package com.pandorina.uzman_ogretmenlik.data.repository

import com.pandorina.uzman_ogretmenlik.core.BaseRepository
import com.pandorina.uzman_ogretmenlik.data.remote.UzmanOgretmenExamService
import com.pandorina.uzman_ogretmenlik.domain.model.QuestionsResponse
import com.pandorina.uzman_ogretmenlik.domain.model.TestsResponse
import com.pandorina.uzman_ogretmenlik.domain.model.TitlesResponse
import com.pandorina.uzman_ogretmenlik.domain.ExamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ExamRepositoryImpl(
    private val service: UzmanOgretmenExamService
): ExamRepository, BaseRepository() {
    override fun getStudyExamTitles(): Flow<Result<TitlesResponse>> = flow {
        emit(safeApiCall { service.getStudyExamTitles() })
    }.flowOn(Dispatchers.IO)

    override fun getStudyExamTests(title: String): Flow<Result<TestsResponse>> = flow {
        emit(safeApiCall { service.getStudyExamTests(title) })
    }.flowOn(Dispatchers.IO)

    override fun getStudyExamQuestions(
        title: String,
        testNo: Int
    ): Flow<Result<QuestionsResponse>> = flow {
        emit(safeApiCall { service.getStudyExamQuestions(title, testNo) })
    }.flowOn(Dispatchers.IO)

    override fun getTrialExamTests(): Flow<Result<TestsResponse>> = flow {
        emit(safeApiCall { service.getTrialExamTests() })
    }.flowOn(Dispatchers.IO)

    override fun getTrialExamQuestions(testNo: Int): Flow<Result<QuestionsResponse>> = flow {
        emit(safeApiCall { service.getTrialExamQuestions(testNo) })
    }.flowOn(Dispatchers.IO)

    override fun getRandomQuestions(count: Int): Flow<Result<QuestionsResponse>> = flow {
        emit(safeApiCall { service.getRandomQuestions(count) })
    }.flowOn(Dispatchers.IO)

    override fun getExamTime(): Flow<Result<Long>> = flow {
        emit(safeApiCall { service.getExamTime() })
    }.flowOn(Dispatchers.IO)
}
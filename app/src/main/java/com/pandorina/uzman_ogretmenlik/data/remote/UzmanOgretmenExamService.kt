package com.pandorina.uzman_ogretmenlik.data.remote

import com.pandorina.uzman_ogretmenlik.domain.model.QuestionsResponse
import com.pandorina.uzman_ogretmenlik.domain.model.TestsResponse
import com.pandorina.uzman_ogretmenlik.domain.model.TitlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UzmanOgretmenExamService {
    @GET("study_exams/titles")
    suspend fun getStudyExamTitles(): Response<TitlesResponse>

    @GET("study_exams/tests")
    suspend fun getStudyExamTests(
        @Query("title") title: String
    ): Response<TestsResponse>

    @GET("study_exams/questions")
    suspend fun getStudyExamQuestions(
        @Query("title") title: String,
        @Query("test_no") testNo: Int
    ): Response<QuestionsResponse>

    @GET("trial_exams/tests")
    suspend fun getTrialExamTests(): Response<TestsResponse>

    @GET("trial_exams/questions")
    suspend fun getTrialExamQuestions(
        @Query("test_no") testNo: Int
    ): Response<QuestionsResponse>

    @GET("random_questions")
    suspend fun getRandomQuestions(
        @Query("count") count: Int
    ): Response<QuestionsResponse>

    @GET("exam_time")
    suspend fun getExamTime(): Response<Long>
}
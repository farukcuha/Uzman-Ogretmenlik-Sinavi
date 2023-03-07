package com.pandorina.uzman_ogretmenlik.domain

import com.pandorina.uzman_ogretmenlik.data.local.StatisticEntity
import kotlinx.coroutines.flow.Flow

interface StatisticRepository {

    suspend fun insertExamResult(title: String, correctCount: Int, wrongCount: Int)

    suspend fun clearExamResults()

    fun getAllExamResults(): Flow<List<StatisticEntity>>

    fun getTotalCorrectCount(): Flow<Int?>

    fun getTotalWrongCount(): Flow<Int?>
}
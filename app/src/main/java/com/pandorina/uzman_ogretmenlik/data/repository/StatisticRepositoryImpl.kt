package com.pandorina.uzman_ogretmenlik.data.repository

import com.pandorina.uzman_ogretmenlik.data.local.StatisticDao
import com.pandorina.uzman_ogretmenlik.data.local.StatisticEntity
import com.pandorina.uzman_ogretmenlik.domain.StatisticRepository
import kotlinx.coroutines.flow.Flow

class StatisticRepositoryImpl(
    private val dao: StatisticDao
): StatisticRepository {

    override suspend fun insertExamResult(title: String, correctCount: Int, wrongCount: Int) {
        dao.insertExamResult(StatisticEntity(
            title = title,
            correctCount = correctCount,
            wrongCount = wrongCount
        ))
    }

    override suspend fun clearExamResults() {
        dao.clearExamResults()
    }

    override fun getAllExamResults(): Flow<List<StatisticEntity>> {
        return dao.getAllExamResults()
    }

    override fun getTotalCorrectCount(): Flow<Int?> {
        return dao.getTotalCorrectCount()
    }

    override fun getTotalWrongCount(): Flow<Int?> {
        return dao.getTotalWrongCount()
    }
}
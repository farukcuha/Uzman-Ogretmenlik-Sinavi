package com.pandorina.uzman_ogretmenlik.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamResult(statisticEntity: StatisticEntity)

    @Query("DELETE FROM statistic_table")
    suspend fun clearExamResults()

    @Query("SELECT * FROM statistic_table")
    fun getAllExamResults(): Flow<List<StatisticEntity>>

    @Query("SELECT sum(correct_count) FROM statistic_table")
    fun getTotalCorrectCount(): Flow<Int?>

    @Query("SELECT sum(wrong_count) FROM statistic_table")
    fun getTotalWrongCount(): Flow<Int?>
}
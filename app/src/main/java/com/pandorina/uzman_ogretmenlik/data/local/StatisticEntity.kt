package com.pandorina.uzman_ogretmenlik.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "statistic_table", primaryKeys = ["title"])
data class StatisticEntity(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "correct_count") val correctCount: Int,
    @ColumnInfo(name = "wrong_count") val wrongCount: Int
)

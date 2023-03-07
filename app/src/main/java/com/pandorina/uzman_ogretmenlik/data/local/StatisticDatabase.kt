package com.pandorina.uzman_ogretmenlik.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StatisticEntity::class], version = 1, exportSchema = true)
abstract class StatisticDatabase: RoomDatabase() {

    abstract fun getStatisticDao(): StatisticDao
}
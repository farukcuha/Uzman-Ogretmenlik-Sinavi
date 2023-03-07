package com.pandorina.uzman_ogretmenlik.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pandorina.uzman_ogretmenlik.data.repository.StatisticRepositoryImpl
import com.pandorina.uzman_ogretmenlik.domain.StatisticRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): StatisticDatabase {
        return Room.databaseBuilder(context, StatisticDatabase::class.java, "statistic_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideStatisticDao(
        database: StatisticDatabase
    ): StatisticDao {
        return database.getStatisticDao()
    }

    @Provides
    @Singleton
    fun provideStatisticRepository(
        dao: StatisticDao
    ): StatisticRepository {
        return StatisticRepositoryImpl(dao)
    }
}
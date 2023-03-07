package com.pandorina.uzman_ogretmenlik.data.remote

import com.pandorina.uzman_ogretmenlik.BuildConfig
import com.pandorina.uzman_ogretmenlik.data.repository.ExamRepositoryImpl
import com.pandorina.uzman_ogretmenlik.domain.ExamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit):
            UzmanOgretmenExamService = retrofit.create(UzmanOgretmenExamService::class.java)

    @Provides
    @Singleton
    fun provideExamRepository(service: UzmanOgretmenExamService):
            ExamRepository = ExamRepositoryImpl(service)
}
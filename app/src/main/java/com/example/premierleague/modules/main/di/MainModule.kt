package com.example.premierleague.modules.main.di

import com.example.premierleague.modules.main.data.source.remote.MainService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
object MainModule {
    @ViewModelScoped
    @Provides
    fun provideWeatherService(retrofit: Retrofit): MainService {
        return retrofit.create(MainService::class.java)
    }
}
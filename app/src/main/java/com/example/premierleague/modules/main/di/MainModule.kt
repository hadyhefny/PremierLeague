package com.example.premierleague.modules.main.di

import com.example.premierleague.core.data.source.local.FavoriteMatchesDao
import com.example.premierleague.core.data.source.remote.MainService
import com.example.premierleague.modules.main.data.repository.MainRepositoryImpl
import com.example.premierleague.modules.main.data.source.local.FavoriteMatchesLocalDs
import com.example.premierleague.modules.main.data.source.remote.FavoriteMatchesRemoteDs
import com.example.premierleague.modules.main.domain.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ViewModelComponent::class)
@Module
object MainModule {
    @ViewModelScoped
    @Provides
    fun provideMainRepositoryImpl(
        favoriteMatchesRemoteDs: FavoriteMatchesRemoteDs,
        favoriteMatchesLocalDs: FavoriteMatchesLocalDs,
        dispatcher: CoroutineDispatcher
    ): MainRepository {
        return MainRepositoryImpl(favoriteMatchesRemoteDs, favoriteMatchesLocalDs, dispatcher)
    }

    @ViewModelScoped
    @Provides
    fun provideFavoriteMatchesLocalDs(favoriteMatchesDao: FavoriteMatchesDao): FavoriteMatchesLocalDs {
        return FavoriteMatchesLocalDs(favoriteMatchesDao)
    }

    @ViewModelScoped
    @Provides
    fun provideFavoriteMatchesRemoteDs(mainService: MainService): FavoriteMatchesRemoteDs {
        return FavoriteMatchesRemoteDs(mainService)
    }
}
package com.example.premierleague.modules.main.data.repository

import com.example.premierleague.modules.main.data.mapper.toEntity
import com.example.premierleague.modules.main.data.source.remote.MainService
import com.example.premierleague.modules.main.domain.entity.MatchesEntity
import com.example.premierleague.modules.main.domain.repository.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mainService: MainService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MainRepository {
    override suspend fun getMatches(): MatchesEntity {
        return withContext(ioDispatcher) {
            mainService.getMatches().toEntity(isFavorite = false)
        }
    }
}
package com.example.premierleague.modules.main.data.repository

import com.example.premierleague.core.data.model.api.MatchesItem
import com.example.premierleague.modules.main.data.mapper.toDto
import com.example.premierleague.modules.main.data.mapper.toEntity
import com.example.premierleague.modules.main.data.source.local.FavoriteMatchesLocalDs
import com.example.premierleague.modules.main.data.source.remote.FavoriteMatchesRemoteDs
import com.example.premierleague.modules.main.domain.entity.MatchesEntity
import com.example.premierleague.modules.main.domain.repository.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val favoriteMatchesRemoteDs: FavoriteMatchesRemoteDs,
    private val favoriteMatchesLocalDs: FavoriteMatchesLocalDs,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MainRepository {
    override suspend fun getMatches(): MatchesEntity {
        return withContext(ioDispatcher) {
            val remoteMatches = favoriteMatchesRemoteDs.getMatches()
            val localMatches = favoriteMatchesLocalDs.getFavoriteMatches()
            val localIds = localMatches.map { it.id }
            if (localMatches.isNotEmpty()) {
                val remoteFavoriteMatches: List<MatchesItem?>? = remoteMatches.matches?.filter {
                    it?.id in localIds
                }
        }
    }
}
package com.example.premierleague.modules.main.data.repository

import com.example.premierleague.core.data.model.api.MatchesItem
import com.example.premierleague.modules.main.data.mapper.toDto
import com.example.premierleague.modules.main.data.mapper.toEntity
import com.example.premierleague.modules.main.data.source.local.FavoriteMatchesLocalDs
import com.example.premierleague.modules.main.data.source.remote.FavoriteMatchesRemoteDs
import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.MatchesEntity
import com.example.premierleague.modules.main.domain.entity.MatchesParam
import com.example.premierleague.modules.main.domain.repository.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val favoriteMatchesRemoteDs: FavoriteMatchesRemoteDs,
    private val favoriteMatchesLocalDs: FavoriteMatchesLocalDs,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MainRepository {
    override suspend fun getMatches(matchesParam: MatchesParam): Flow<MatchesEntity> {
        val remoteMatches = withContext(ioDispatcher) {
            favoriteMatchesRemoteDs.getMatches(matchesParam)
        }
        return favoriteMatchesLocalDs.getFavoriteMatches().distinctUntilChanged()
            .map { localMatches ->
                val localIds = localMatches.map { it.id }
                if (localMatches.isNotEmpty()) {
                    val remoteFavoriteMatches: List<MatchesItem?>? =
                        remoteMatches.matches?.filter {
                            it?.id in localIds
                        }
                    remoteFavoriteMatches?.map {
                        it?.let {
                            favoriteMatchesLocalDs.insertMatch(it.toEntity(true).toDto())
                        }
                    }
                    val matchesEntity: List<MatchEntity?>? = remoteMatches.matches?.map {
                        it?.toEntity(remoteFavoriteMatches?.contains(it) == true)
                    }
                    MatchesEntity((matchesEntity))
                } else {
                    val matchesEntity: List<MatchEntity?>? = remoteMatches.matches?.map {
                        it?.toEntity(false)
                    }
                    MatchesEntity((matchesEntity))
                }
            }.flowOn(ioDispatcher)
    }

    override suspend fun addToFavorite(matchEntity: MatchEntity) {
        favoriteMatchesLocalDs.insertMatch(matchEntity.toDto())
    }

    override suspend fun removeFromFavorite(matchEntity: MatchEntity) {
        favoriteMatchesLocalDs.deleteMatch(matchEntity.toDto())
    }
}
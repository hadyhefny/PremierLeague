package com.example.premierleague.modules.main.domain.repository

import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.MatchesEntity
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getMatches(): Flow<MatchesEntity>
    suspend fun addToFavorite(matchEntity: MatchEntity)
    suspend fun removeFromFavorite(matchEntity: MatchEntity)
}
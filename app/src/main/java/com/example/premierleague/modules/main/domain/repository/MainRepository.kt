package com.example.premierleague.modules.main.domain.repository

import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.MatchesEntity
import com.example.premierleague.modules.main.domain.entity.MatchesParam
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getMatches(matchesParam: MatchesParam): Flow<MatchesEntity>
    suspend fun addToFavorite(matchEntity: MatchEntity)
    suspend fun removeFromFavorite(matchEntity: MatchEntity)
}
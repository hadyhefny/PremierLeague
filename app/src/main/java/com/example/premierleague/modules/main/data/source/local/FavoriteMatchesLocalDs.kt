package com.example.premierleague.modules.main.data.source.local

import com.example.premierleague.core.data.model.dto.MatchDto
import com.example.premierleague.core.data.source.local.FavoriteMatchesDao
import javax.inject.Inject

class FavoriteMatchesLocalDs @Inject constructor(private val favoriteMatchesDao: FavoriteMatchesDao) {
    suspend fun getFavoriteMatches(): List<MatchDto> {
        return favoriteMatchesDao.getFavoriteMatches()
    }

    suspend fun insertMatch(matchDto: MatchDto) {
        favoriteMatchesDao.insertMatch(matchDto)
    }

    suspend fun deleteMatch(matchDto: MatchDto) {
        favoriteMatchesDao.deleteMatch(matchDto)
    }
}
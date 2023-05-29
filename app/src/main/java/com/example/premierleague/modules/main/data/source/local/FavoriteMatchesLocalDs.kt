package com.example.premierleague.modules.main.data.source.local

import com.example.premierleague.core.data.model.dto.MatchDto
import com.example.premierleague.core.data.source.local.FavoriteMatchesDao
import com.example.premierleague.core.extension.getFormattedDate
import com.example.premierleague.modules.main.domain.entity.MatchesParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteMatchesLocalDs @Inject constructor(private val favoriteMatchesDao: FavoriteMatchesDao) {
    fun getFavoriteMatches(matchesParam: MatchesParam): Flow<List<MatchDto>> {
        return favoriteMatchesDao.getFavoriteMatches(status = matchesParam.status, dateFrom = matchesParam.dateFrom?.getFormattedDate(), matchesParam.dateTo?.getFormattedDate())
    }

    suspend fun insertMatch(matchDto: MatchDto) {
        favoriteMatchesDao.insertMatch(matchDto)
    }

    suspend fun deleteMatch(matchDto: MatchDto) {
        favoriteMatchesDao.deleteMatch(matchDto)
    }
}
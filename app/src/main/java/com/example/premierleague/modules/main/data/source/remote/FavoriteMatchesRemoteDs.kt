package com.example.premierleague.modules.main.data.source.remote

import com.example.premierleague.core.data.model.api.MainResponse
import com.example.premierleague.core.data.source.remote.MainService
import com.example.premierleague.modules.main.domain.entity.MatchesParam
import javax.inject.Inject

class FavoriteMatchesRemoteDs @Inject constructor(private val mainService: MainService) {
    suspend fun getMatches(matchesParam: MatchesParam): MainResponse {
        return mainService.getMatches(
            dateFrom = matchesParam.dateFrom,
            dateTo = matchesParam.dateTo,
            status = matchesParam.status
        )
    }
}
package com.example.premierleague.modules.main.data.source.remote

import com.example.premierleague.core.data.source.remote.MainService
import com.example.premierleague.core.data.model.api.MainResponse
import javax.inject.Inject

class FavoriteMatchesRemoteDs @Inject constructor(private val mainService: MainService) {
    suspend fun getMatches(): MainResponse {
        return mainService.getMatches()
    }
}
package com.example.premierleague.modules.main.domain.repository

import com.example.premierleague.modules.main.domain.entity.MatchesEntity

interface MainRepository {
    suspend fun getMatches(): MatchesEntity
}
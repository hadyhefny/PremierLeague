package com.example.premierleague.modules.main.presentation.model

import com.example.premierleague.modules.main.domain.entity.MatchesEntity

data class MainUiState(
    val isLoading: Boolean = false,
    val isFavoriteSelected: Boolean = false,
    val matchesEntity: MatchesEntity = MatchesEntity(listOf()),
    val favoritesMatches: MatchesEntity = MatchesEntity(listOf()),
    val pinnedMatches: MatchesEntity = MatchesEntity(listOf()),
    val pinnedFavoritesMatches: MatchesEntity = MatchesEntity(listOf())
)

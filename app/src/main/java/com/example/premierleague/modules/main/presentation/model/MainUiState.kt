package com.example.premierleague.modules.main.presentation.model

import com.example.premierleague.modules.main.domain.entity.MatchesEntity

data class MainUiState(
    val isLoading: Boolean = false,
    val matchesEntity: MatchesEntity = MatchesEntity(listOf())
)

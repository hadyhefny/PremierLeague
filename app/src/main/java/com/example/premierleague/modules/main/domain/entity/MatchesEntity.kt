package com.example.premierleague.modules.main.domain.entity

data class MatchesEntity(
    val matches: List<MatchEntity?>?
)

data class MatchEntity(
    val id: Int?,
    val date: String?,
    val status: String?,
    val score: Score?,
    val competitors: Competitors?,
    val isFavorite: Boolean
)

data class Score(
    val home: Int?,
    val away: Int?
)

data class Competitors(
    val home: String?,
    val away: String?
)
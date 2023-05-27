package com.example.premierleague.modules.main.data.mapper

import com.example.premierleague.core.data.model.api.MatchesItem
import com.example.premierleague.core.data.model.dto.MatchDto
import com.example.premierleague.modules.main.domain.entity.Competitors
import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.Score

fun MatchesItem.toEntity(isFavorite: Boolean) =
    MatchEntity(
        id = id,
        date = utcDate,
        status = status,
        score = Score(
            home = score?.fullTime?.homeTeam,
            away = score?.fullTime?.awayTeam
        ),
        competitors = Competitors(
            home = homeTeam?.name,
            away = awayTeam?.name
        ),
        isFavorite = isFavorite
    )

fun List<MatchDto>.toEntity() =
    this.map {
        MatchEntity(
            id = it.id,
            date = it.date,
            status = it.status,
            score = Score(home = it.homeScore, away = it.awayScore),
            competitors = Competitors(home = it.homeName, away = it.awayName),
            isFavorite = true
        )
    }

fun MatchEntity.toDto() = MatchDto(
    id = id ?: 0, date = date, status = status, homeScore = score?.home,
    awayScore = score?.away,
    homeName = competitors?.home,
    awayName = competitors?.away
)
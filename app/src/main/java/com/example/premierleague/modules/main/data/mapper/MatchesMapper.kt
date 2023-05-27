package com.example.premierleague.modules.main.data.mapper

import com.example.premierleague.modules.main.data.model.MainResponse
import com.example.premierleague.modules.main.data.model.MatchesItem
import com.example.premierleague.modules.main.domain.entity.Competitors
import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.MatchesEntity
import com.example.premierleague.modules.main.domain.entity.Score

fun MainResponse.toEntity(isFavorite: Boolean) = MatchesEntity(
    matches = matches.toEntity(isFavorite)
)

fun List<MatchesItem?>?.toEntity(isFavorite: Boolean) =
    this?.map {
        MatchEntity(
            id = it?.id,
            date = it?.utcDate,
            status = it?.status,
            score = Score(
                home = it?.score?.fullTime?.homeTeam,
                away = it?.score?.fullTime?.awayTeam
            ),
            competitors = Competitors(
                home = it?.homeTeam?.name,
                away = it?.awayTeam?.name
            ),
            isFavorite = isFavorite
        )
    }
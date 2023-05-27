package com.example.premierleague.core.data.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.premierleague.core.data.source.local.FAVORITE_MATCHES_TABLE

@Entity(tableName = FAVORITE_MATCHES_TABLE)
data class MatchDto(
    @PrimaryKey val id: Long,
    val date: String?,
    val status: String?,
    val homeScore: Int?,
    val awayScore: Int?,
    val homeName: String?,
    val awayName: String?
)

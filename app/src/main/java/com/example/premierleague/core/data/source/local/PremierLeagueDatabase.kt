package com.example.premierleague.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.premierleague.core.data.model.dto.MatchDto

@Database(entities = [MatchDto::class], version = 1)
abstract class PremierLeagueDatabase : RoomDatabase() {
    abstract fun favoriteMatchesDao(): FavoriteMatchesDao
}

const val FAVORITE_MATCHES_TABLE = "favorite_matches"
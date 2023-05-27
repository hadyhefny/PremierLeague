package com.example.premierleague.core.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.premierleague.core.data.model.dto.MatchDto

@Dao
interface FavoriteMatchesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(matchDto: MatchDto)

    @Query("SELECT * FROM favorite_matches WHERE id = id")
    suspend fun getFavoriteMatches(): List<MatchDto>

    @Delete
    suspend fun deleteMatch(matchDto: MatchDto)

}
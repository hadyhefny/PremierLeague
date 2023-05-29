package com.example.premierleague.core.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.premierleague.core.data.model.dto.MatchDto
import com.example.premierleague.modules.main.domain.entity.MatchesParam
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMatchesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(matchDto: MatchDto)

    @Query("SELECT * FROM favorite_matches WHERE (:status IS NULL) OR (:status IS NOT NULL AND status = :status) AND (:dateFrom IS Null AND :dateTo IS NULL) OR (:dateFrom IS NOT NULL AND :dateTo IS NOT NULL AND date BETWEEN :dateFrom AND :dateTo)")
    fun getFavoriteMatches(
        status: String?,
        dateFrom: String?,
        dateTo: String?
    ): Flow<List<MatchDto>>

    @Delete
    suspend fun deleteMatch(matchDto: MatchDto)

}
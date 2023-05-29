package com.example.premierleague.core.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.premierleague.core.data.model.dto.MatchDto
import io.mockk.MockKAnnotations
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteMatchesDaoTest {
    private lateinit var db: PremierLeagueDatabase
    private lateinit var favoriteMatchesDao: FavoriteMatchesDao

    @Before
    fun createDb() {
        MockKAnnotations.init(this)
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, PremierLeagueDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        favoriteMatchesDao = db.favoriteMatchesDao()
    }

    @Test
    fun insertMatch_returnMatch(): Unit = runBlocking {
        // given
        val matchDto = MatchDto(
            123456,
            "May 28 Mon",
            "FINISHED",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Egypt"
        )
        // when
        favoriteMatchesDao.insertMatch(matchDto)
        // then
        val matches: List<MatchDto> =
            favoriteMatchesDao.getFavoriteMatches(null, null, null).first()
        assertEquals(matchDto, matches[0])
    }

    @Test
    fun removeMatch_returnNothing(): Unit = runBlocking {
        // given
        val matchDto = MatchDto(
            123456,
            "May 28 Mon",
            "FINISHED",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Egypt"
        )
        favoriteMatchesDao.insertMatch(matchDto)
        // when
        favoriteMatchesDao.deleteMatch(matchDto)
        // then
        val matches: List<MatchDto> =
            favoriteMatchesDao.getFavoriteMatches(null, null, null).first()
        assertTrue(matches.isEmpty())
    }

    @Test
    fun getMatches_returnMatchesStatusFinishedList(): Unit = runBlocking {
        // given
        val matchDto1 = MatchDto(
            123456,
            "May 28 Mon",
            "FINISHED",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Egypt"
        )
        val matchDto2 = MatchDto(
            654321,
            "May 28 Mon",
            "IN_PLAY",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Egypt"
        )
        favoriteMatchesDao.insertMatch(matchDto1)
        favoriteMatchesDao.insertMatch(matchDto2)
        // when
        favoriteMatchesDao.getFavoriteMatches(status = "FINISHED", null, null)
        // then
        val matches: List<MatchDto> =
            favoriteMatchesDao.getFavoriteMatches(null, null, null).first()
        assertEquals(matchDto1, matches[0])
    }

    @After
    fun closeDb() {
        db.close()
    }
}
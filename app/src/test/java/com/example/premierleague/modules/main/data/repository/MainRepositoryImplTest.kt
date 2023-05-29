package com.example.premierleague.modules.main.data.repository

import com.example.premierleague.core.data.model.api.AwayTeam
import com.example.premierleague.core.data.model.api.FullTime
import com.example.premierleague.core.data.model.api.HomeTeam
import com.example.premierleague.core.data.model.api.MainResponse
import com.example.premierleague.core.data.model.api.MatchesItem
import com.example.premierleague.core.data.model.api.Score
import com.example.premierleague.core.data.model.dto.MatchDto
import com.example.premierleague.modules.main.data.source.local.FavoriteMatchesLocalDs
import com.example.premierleague.modules.main.data.source.remote.FavoriteMatchesRemoteDs
import com.example.premierleague.modules.main.domain.entity.Competitors
import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.MatchesEntity
import com.example.premierleague.modules.main.domain.entity.MatchesParam
import com.example.premierleague.modules.main.domain.repository.MainRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainRepositoryImplTest {

    private lateinit var SUT: MainRepository

    @MockK
    lateinit var favoritesRemoteDs: FavoriteMatchesRemoteDs

    @RelaxedMockK
    lateinit var favoritesLocalDs: FavoriteMatchesLocalDs

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = MainRepositoryImpl(favoritesRemoteDs, favoritesLocalDs, testDispatcher)
    }

    @Test
    fun getMatches() = runTest(testDispatcher) {
        // given
        val matchDto = MatchDto(
            123456,
            "May 28 Mon",
            "FINISHED",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Ghana"
        )
        val matchEntity = MatchEntity(
            123456,
            "Fri 5 Aug",
            "FINISHED",
            score = com.example.premierleague.modules.main.domain.entity.Score(home = 2, away = 1),
            competitors = Competitors(home = "Egypt", "Ghana"),
            isFavorite = true,
            isDate = false
        )
        val mainResponse = MainResponse(
            matches = arrayListOf(
                MatchesItem(
                    id = 123456,
                    utcDate = "2022-08-05T19:00:00Z",
                    status = "FINISHED",
                    homeTeam = HomeTeam(name = "Egypt"),
                    awayTeam = AwayTeam(name = "Ghana"),
                    score = Score(fullTime = FullTime(homeTeam = 2, awayTeam = 1))
                )
            )
        )
        // when
        coEvery { favoritesLocalDs.insertMatch(matchDto) }.just(Runs)
        coEvery { favoritesLocalDs.getFavoriteMatches(MatchesParam()) }.returns(flow {
            emit(
                arrayListOf(matchDto)
            )
        })
        coEvery { favoritesRemoteDs.getMatches(MatchesParam()) }.returns(mainResponse)
        // then
        val matchesEntity: MatchesEntity = SUT.getMatches(MatchesParam()).first()
        assertEquals(matchesEntity.matches?.get(0), matchEntity)
    }

    @Test
    fun addToFavorites() = runTest(testDispatcher) {
        // given
        val matchDto = MatchDto(
            123456,
            "May 28 Mon",
            "FINISHED",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Ghana"
        )
        val matchEntity = MatchEntity(
            123456,
            "Fri 5 Aug",
            "FINISHED",
            score = com.example.premierleague.modules.main.domain.entity.Score(home = 2, away = 1),
            competitors = Competitors(home = "Egypt", "Ghana"),
            isFavorite = true,
            isDate = false
        )
        val mainResponse = MainResponse(
            matches = arrayListOf(
                MatchesItem(
                    id = 123456,
                    utcDate = "2022-08-05T19:00:00Z",
                    status = "FINISHED",
                    homeTeam = HomeTeam(name = "Egypt"),
                    awayTeam = AwayTeam(name = "Ghana"),
                    score = Score(fullTime = FullTime(homeTeam = 2, awayTeam = 1))
                )
            )
        )
        // when
        SUT.addToFavorite(matchEntity)
        coEvery { favoritesLocalDs.getFavoriteMatches(MatchesParam()) }.returns(flow {
            emit(
                arrayListOf(matchDto)
            )
        })
        coEvery { favoritesRemoteDs.getMatches(MatchesParam()) }.returns(mainResponse)
        // then
        val entity: MatchEntity? = SUT.getMatches(MatchesParam()).first().matches?.get(0)
        assertEquals(entity, matchEntity)
    }

    @Test
    fun removeFromFavorites() = runTest(testDispatcher) {
        // given
        val matchDto = MatchDto(
            123456,
            "May 28 Mon",
            "FINISHED",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Ghana"
        )
        val matchEntity = MatchEntity(
            123456,
            "Fri 5 Aug",
            "FINISHED",
            score = com.example.premierleague.modules.main.domain.entity.Score(home = 2, away = 1),
            competitors = Competitors(home = "Egypt", "Ghana"),
            isFavorite = true,
            isDate = false
        )
        val mainResponse = MainResponse(
            matches = arrayListOf(
                MatchesItem(
                    id = 123456,
                    utcDate = "2022-08-05T19:00:00Z",
                    status = "FINISHED",
                    homeTeam = HomeTeam(name = "Egypt"),
                    awayTeam = AwayTeam(name = "Ghana"),
                    score = Score(fullTime = FullTime(homeTeam = 2, awayTeam = 1))
                )
            )
        )
        favoritesLocalDs.insertMatch(matchDto)
        // when
        SUT.removeFromFavorite(matchEntity)
        coEvery { favoritesLocalDs.getFavoriteMatches(MatchesParam()) }.returns(flow {
            emit(
                arrayListOf()
            )
        })
        coEvery { favoritesRemoteDs.getMatches(MatchesParam()) }.returns(mainResponse)
        // then
        val entity: MatchEntity? = SUT.getMatches(MatchesParam()).first().matches?.get(0)
        assertEquals(entity?.isFavorite, false)
    }
}
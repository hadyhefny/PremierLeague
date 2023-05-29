package com.example.premierleague.modules.main.domain.interactor

import com.example.premierleague.core.extension.getDate
import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.MatchesEntity
import com.example.premierleague.modules.main.domain.entity.MatchesParam
import com.example.premierleague.modules.main.domain.repository.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(matchesParam: MatchesParam): Flow<MatchesEntity> {
        return mainRepository.getMatches(matchesParam).map {
            it.matches?.groupBy {
                it?.date
            }
        }.map { group ->
            val groupedMatches = arrayListOf<MatchEntity?>()
            group?.map {
                groupedMatches.add(
                    MatchEntity(
                        null,
                        it.key,
                        null,
                        null,
                        null,
                        it.value.any { it?.isFavorite == true },
                        isDate = true
                    )
                )
                groupedMatches.addAll(it.value)
            }
            MatchesEntity(
                matches = groupedMatches.filterOutTodayOrTomorrow(),
                favoriteMatches = groupedMatches.filterFavoriteMatches(),
                pinnedMatches = groupedMatches.filterPinnedMatches(),
                pinnedFavoritesMatches = groupedMatches.filterPinnedFavoriteMatches()
            )
        }.flowOn(ioDispatcher)
    }

    private fun List<MatchEntity?>?.filterFavoriteMatches(): List<MatchEntity?>? {
        return this?.filter { it?.isFavorite == true }
    }

    private fun List<MatchEntity?>?.filterPinnedFavoriteMatches(): List<MatchEntity?>? {
        return this?.filter {
            isTodayOrTomorrow(
                it?.date
            ) && it?.isDate == false && it.isFavorite
        }
    }

    private fun List<MatchEntity?>?.filterPinnedMatches(): List<MatchEntity?>? {
        return this?.filter {
            isTodayOrTomorrow(
                it?.date
            ) && it?.isDate == false
        }
    }

    private fun List<MatchEntity?>?.filterOutTodayOrTomorrow(): List<MatchEntity?>? {
        return this?.filter {
            !isTodayOrTomorrow(it?.date)
        }
    }

    private fun isTodayOrTomorrow(rawDate: String?): Boolean {
        val matchDate: Date? = rawDate?.getDate()
        return matchDate?.let {
            val matchCalendar = Calendar.getInstance()
            matchCalendar.time = matchDate
            val matchMonth = matchCalendar.get(Calendar.MONTH)
            val matchDay = matchCalendar.get(Calendar.DAY_OF_MONTH)

            val calendar = Calendar.getInstance()
            val calMonth = calendar.get(Calendar.MONTH)
            val calDay = calendar.get(Calendar.DAY_OF_MONTH)
            (calMonth == matchMonth)
                    && (calDay == matchDay || calDay + 1 == matchDay)
        } ?: run {
            false
        }
    }
}
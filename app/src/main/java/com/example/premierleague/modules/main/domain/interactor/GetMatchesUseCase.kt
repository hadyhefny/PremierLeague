package com.example.premierleague.modules.main.domain.interactor

import com.example.premierleague.core.extension.getFormattedDate
import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.MatchesEntity
import com.example.premierleague.modules.main.domain.entity.MatchesParam
import com.example.premierleague.modules.main.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend operator fun invoke(matchesParam: MatchesParam): Flow<MatchesEntity> {
        return mainRepository.getMatches(matchesParam).map {
            it.matches?.groupBy {
                it?.date?.getFormattedDate()
            }
        }.map { group ->
            val groupedList = arrayListOf<MatchEntity?>()
            group?.map {
                groupedList.add(
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
                groupedList.addAll(it.value)
            }
            MatchesEntity(matches = groupedList)
        }
    }
}
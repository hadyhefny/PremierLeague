package com.example.premierleague.modules.main.domain.interactor

import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.MatchesEntity
import com.example.premierleague.modules.main.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend operator fun invoke(): Flow<MatchesEntity> {
        return mainRepository.getMatches().map {
            it.matches?.groupBy {
                it?.date
            }
        }.map { group ->
            val groupedList = arrayListOf<MatchEntity?>()
            group?.map {
                groupedList.add(MatchEntity(null, it.key, null, null, null, false, isDate = true))
                groupedList.addAll(it.value)
            }
            MatchesEntity(matches = groupedList)
        }
    }
}
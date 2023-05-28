package com.example.premierleague.modules.main.domain.interactor

import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.repository.MainRepository
import javax.inject.Inject

class ChangeFavoriteStatusUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend operator fun invoke(matchEntity: MatchEntity) {
        return if (matchEntity.isFavorite) {
            mainRepository.removeFromFavorite(matchEntity)
        } else {
            mainRepository.addToFavorite(matchEntity)
        }
    }
}
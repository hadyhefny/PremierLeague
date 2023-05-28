package com.example.premierleague.modules.main.presentation.viewmodel

import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.premierleague.core.extension.getDate
import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.MatchesParam
import com.example.premierleague.modules.main.domain.interactor.ChangeFavoriteStatusUseCase
import com.example.premierleague.modules.main.domain.interactor.GetMatchesUseCase
import com.example.premierleague.modules.main.presentation.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val changeFavoriteStatusUseCase: ChangeFavoriteStatusUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState>
        get() = _uiState

    private val _effect = MutableSharedFlow<String>()
    val effect: SharedFlow<String>
        get() = _effect

    init {
        getMatches()
    }

    fun changeMatchFavoriteStatus(matchEntity: MatchEntity) {
        viewModelScope.launch {
            changeFavoriteStatusUseCase.invoke(matchEntity)
        }

    }

    fun changeFavoriteSelection(isFav: Boolean) {
        _uiState.value = _uiState.value.copy(isFavoriteSelected = isFav)
    }

    fun getMatches(matchesParam: MatchesParam = MatchesParam()) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                getMatchesUseCase(matchesParam)
                    .collectLatest {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            matchesEntity = it.copy(matches = it.matches?.filter {
                                !isTodayOrTomorrow(
                                    it?.date
                                )
                            }),
                            favoritesMatches = it.copy(matches = it.matches?.filter { it?.isFavorite == true }),
                            pinnedMatches = it.copy(matches = it.matches?.filter {
                                isTodayOrTomorrow(
                                    it?.date
                                ) && it?.isDate == false
                            }),
                            pinnedFavoritesMatches = it.copy(matches = it.matches?.filter {
                                isTodayOrTomorrow(
                                    it?.date
                                ) && it?.isDate == false && it.isFavorite
                            })
                        )
                    }
            } catch (e: Exception) {
                _effect.emit(e.message ?: "")
            }
        }
    }

    private fun isTodayOrTomorrow(rawDate: String?): Boolean {
        val date: Date? = rawDate?.getDate()
        return date?.let {
            DateUtils.isToday(date.time) || DateUtils.isToday(date.time + DateUtils.DAY_IN_MILLIS)
        } ?: run {
            false
        }
    }
}
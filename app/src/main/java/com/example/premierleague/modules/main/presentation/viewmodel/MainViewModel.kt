package com.example.premierleague.modules.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.premierleague.modules.main.domain.entity.MatchEntity
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

    private fun getMatches() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                getMatchesUseCase()
                    .collectLatest {
                        _uiState.value = _uiState.value.copy(isLoading = false, matchesEntity = it)
                    }
            } catch (e: Exception) {
                _effect.emit(e.message ?: "")
            }
        }
    }

}
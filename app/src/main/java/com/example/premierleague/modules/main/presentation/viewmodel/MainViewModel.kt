package com.example.premierleague.modules.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.premierleague.R
import com.example.premierleague.modules.main.domain.entity.MatchEntity
import com.example.premierleague.modules.main.domain.entity.MatchesParam
import com.example.premierleague.modules.main.domain.interactor.ChangeFavoriteStatusUseCase
import com.example.premierleague.modules.main.domain.interactor.GetMatchesUseCase
import com.example.premierleague.modules.main.presentation.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val changeFavoriteStatusUseCase: ChangeFavoriteStatusUseCase,
) : ViewModel() {

    var matchesJob: Job? = null
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState>
        get() = _uiState

    private val _effect = MutableSharedFlow<Int?>()
    val effect: SharedFlow<Int?>
        get() = _effect

    val filtersState = MutableStateFlow(MatchesParam())

    init {
        collectFiltersState()
    }

    fun changeMatchFavoriteStatus(matchEntity: MatchEntity) {
        viewModelScope.launch {
            changeFavoriteStatusUseCase.invoke(matchEntity)
        }
    }

    fun changeFavoriteSelection(isFav: Boolean) {
        _uiState.value = _uiState.value.copy(isFavoriteSelected = isFav)
    }

    private fun getMatches(matchesParam: MatchesParam = MatchesParam()) {
        matchesJob?.cancel()
        matchesJob = Job()
        viewModelScope.launch(matchesJob!!) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _effect.emit(null)
            try {
                getMatchesUseCase(matchesParam)
                    .collectLatest {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            matchesEntity = it
                        )
                        _effect.emit(null)
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _effect.emit(e.handleError())
            }
        }
    }

    private fun collectFiltersState() {
        viewModelScope.launch {
            filtersState.collectLatest {
                if (it.dateFrom == null || it.dateTo == null) {
                    getMatches(it.copy(dateFrom = null, dateTo = null))
                } else {
                    getMatches(it)
                }
            }
        }
    }

    private fun Throwable?.handleError(): Int {
        return if (this is IOException) {
            R.string.internet_error
        } else {
            R.string.something_went_wrong_error
        }
    }

    override fun onCleared() {
        matchesJob?.cancel()
        super.onCleared()
    }
}
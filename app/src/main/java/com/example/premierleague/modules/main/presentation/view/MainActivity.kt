package com.example.premierleague.modules.main.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.premierleague.databinding.ActivityMainBinding
import com.example.premierleague.modules.main.presentation.adapter.MatchesAdapter
import com.example.premierleague.modules.main.presentation.adapter.PinnedMatchesAdapter
import com.example.premierleague.modules.main.presentation.viewmodel.MainViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private val TAG = "AppDebug"
    private val concatAdapter = ConcatAdapter()

    @Inject
    lateinit var matchesAdapter: MatchesAdapter

    @Inject
    lateinit var pinnedMatchesAdapter: PinnedMatchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        renderState()
        renderEffect()
        initListeners()
    }

    private fun initRecyclerView() {
        with(binding.matchesRv) {
            layoutManager =
                LinearLayoutManager(this@MainActivity)
            adapter = concatAdapter
        }
    }

    private fun initListeners() {
        matchesAdapter.onItemClickListener = {
            viewModel.changeMatchFavoriteStatus(it)
        }
        pinnedMatchesAdapter.onItemClickListener = {
            viewModel.changeMatchFavoriteStatus(it)
        }
        binding.favoritesCb.addOnCheckedStateChangedListener { _, state ->
            viewModel.changeFavoriteSelection(state == MaterialCheckBox.STATE_CHECKED)
        }
    }

    private fun renderState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    binding.clLoading.isVisible = it.isLoading
                    if (it.isFavoriteSelected) {
                        matchesAdapter.submitList(it.favoritesMatches.matches)
                        pinnedMatchesAdapter.submitList(mutableListOf(it.pinnedFavoritesMatches))
                    } else {
                        matchesAdapter.submitList(it.matchesEntity.matches)
                        pinnedMatchesAdapter.submitList(mutableListOf(it.pinnedMatches))
                    }
                    concatAdapter.addAdapter(0, pinnedMatchesAdapter)
                    concatAdapter.addAdapter(matchesAdapter)
                    Log.d(TAG, "renderState: $it")
                }
            }
        }
    }

    private fun renderEffect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collectLatest {
                    Log.d(TAG, "renderEffect: $it")
                }
            }
        }
    }
}
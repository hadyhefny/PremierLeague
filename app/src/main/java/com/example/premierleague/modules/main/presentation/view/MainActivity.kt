package com.example.premierleague.modules.main.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.premierleague.databinding.ActivityMainBinding
import com.example.premierleague.modules.main.presentation.adapter.MatchesAdapter
import com.example.premierleague.modules.main.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var matchesAdapter: MatchesAdapter
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
            adapter = matchesAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity)
        }
    }

    private fun initListeners() {
        matchesAdapter.onItemClickListener = {
            viewModel.changeMatchFavoriteStatus(it)
        }
    }

    private fun renderState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    matchesAdapter.submitList(it.matchesEntity.matches)
                    Log.d("AppDebug", "renderState: $it")
                }
            }
        }
    }

    private fun renderEffect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collectLatest {
                    Log.d("AppDebug", "renderEffect: $it")
                }
            }
        }
    }
}
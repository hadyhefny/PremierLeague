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
import com.example.premierleague.R
import com.example.premierleague.core.extension.formatDate
import com.example.premierleague.databinding.ActivityMainBinding
import com.example.premierleague.modules.main.domain.entity.MatchStatus
import com.example.premierleague.modules.main.presentation.adapter.MatchesAdapter
import com.example.premierleague.modules.main.presentation.adapter.PinnedMatchesAdapter
import com.example.premierleague.modules.main.presentation.dialog.StatusDialog
import com.example.premierleague.modules.main.presentation.viewmodel.MainViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.datepicker.MaterialDatePicker
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

    private var statusDialog: StatusDialog? = null

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
        binding.dateFromChip.setOnClickListener {
            openDatePicker {
                binding.dateFromChip.text = getString(R.string.date_from, it.formatDate())
                binding.dateFromChip.isCloseIconVisible = true
                viewModel.filtersState.value =
                    viewModel.filtersState.value.copy(dateFrom = it.formatDate("yyyy-MM-dd"))
            }
        }
        binding.dateToChip.setOnClickListener {
            openDatePicker {
                binding.dateToChip.text = getString(R.string.date_to, it.formatDate())
                binding.dateToChip.isCloseIconVisible = true
                viewModel.filtersState.value =
                    viewModel.filtersState.value.copy(dateTo = it.formatDate("yyyy-MM-dd"))
            }
        }
        binding.dateFromChip.setOnCloseIconClickListener {
            binding.dateFromChip.text = getString(R.string.from)
            binding.dateFromChip.isCloseIconVisible = false
            viewModel.filtersState.value = viewModel.filtersState.value.copy(dateFrom = null)
        }
        binding.dateToChip.setOnCloseIconClickListener {
            binding.dateToChip.text = getString(R.string.to)
            binding.dateToChip.isCloseIconVisible = false
            viewModel.filtersState.value = viewModel.filtersState.value.copy(dateTo = null)
        }
        binding.statusChip.setOnClickListener {
            openStatusDialog {
                binding.statusChip.text = getString(R.string.match_status, it.name)
                binding.statusChip.isCloseIconVisible = true
                viewModel.filtersState.value = viewModel.filtersState.value.copy(status = it.name)
            }
        }
        binding.statusChip.setOnCloseIconClickListener {
            binding.statusChip.text = getString(R.string.all)
            binding.statusChip.isCloseIconVisible = false
            viewModel.filtersState.value = viewModel.filtersState.value.copy(status = null)
        }
    }

    private fun openDatePicker(onDateSelected: (Long) -> Unit) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(supportFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener {
            onDateSelected(it)
        }
    }

    private fun openStatusDialog(onStatusSelected: (MatchStatus) -> Unit) {
        statusDialog = StatusDialog()
        statusDialog?.onDismissListener = {
            statusDialog = null
        }
        statusDialog?.onItemSelected = {
            onStatusSelected(it)
        }
        statusDialog?.show(supportFragmentManager, StatusDialog::class.java.name)
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
                    binding.clLoading.isVisible = false
                }
            }
        }
    }
}
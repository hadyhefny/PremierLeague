package com.example.premierleague.modules.main.presentation.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.premierleague.R
import com.example.premierleague.databinding.LayoutStatusDialogBinding
import com.example.premierleague.modules.main.domain.entity.MatchStatus
import com.example.premierleague.modules.main.domain.entity.MatchStatus.CANCELLED
import com.example.premierleague.modules.main.domain.entity.MatchStatus.FINISHED
import com.example.premierleague.modules.main.domain.entity.MatchStatus.IN_PLAY
import com.example.premierleague.modules.main.domain.entity.MatchStatus.LIVE
import com.example.premierleague.modules.main.domain.entity.MatchStatus.PAUSED
import com.example.premierleague.modules.main.domain.entity.MatchStatus.POSTPONED
import com.example.premierleague.modules.main.domain.entity.MatchStatus.SCHEDULED
import com.example.premierleague.modules.main.domain.entity.MatchStatus.SUSPENDED
import com.example.premierleague.modules.main.presentation.adapter.StatusAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatusDialog : DialogFragment() {
    private lateinit var binding: LayoutStatusDialogBinding

    @Inject
    lateinit var statusAdapter: StatusAdapter
    var onItemSelected: ((MatchStatus) -> Unit)? = null
    var onDismissListener: (() -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutStatusDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels - resources.displayMetrics.density * 30).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bg_white
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStatusRecyclerView()
        initListener()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }

    private fun initListener() {
        statusAdapter.onItemClickListener = {
            onItemSelected?.invoke(it)
            dismiss()
        }
    }

    private fun initStatusRecyclerView() {
        binding.statusRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val statuses = arrayListOf(
                SCHEDULED,
                LIVE,
                IN_PLAY,
                PAUSED,
                FINISHED,
                POSTPONED,
                SUSPENDED,
                CANCELLED
            )
            statusAdapter.addList(statuses)
            adapter = statusAdapter
        }
    }

}
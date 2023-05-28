package com.example.premierleague.modules.main.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.premierleague.databinding.ItemStatusBinding
import com.example.premierleague.modules.main.domain.entity.MatchStatus
import javax.inject.Inject

class StatusAdapter @Inject constructor() :
    RecyclerView.Adapter<ViewHolder>() {
    private var statuses: List<MatchStatus> = emptyList()
    var onItemClickListener: ((status: MatchStatus) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return statuses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as StatusViewHolder).bind(statuses[position])
    }

    fun addList(statuses: List<MatchStatus>) {
        this.statuses = statuses
        notifyDataSetChanged()
    }

    inner class StatusViewHolder(private val binding: ItemStatusBinding) :
        ViewHolder(binding.root) {
        fun bind(status: MatchStatus) {
            binding.statusTv.text = status.name
            binding.statusTv.setOnClickListener {
                onItemClickListener?.invoke(status)
            }
        }
    }
}

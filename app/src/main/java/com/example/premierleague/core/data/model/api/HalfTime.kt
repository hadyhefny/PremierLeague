package com.example.premierleague.core.data.model.api

import com.google.gson.annotations.SerializedName

data class HalfTime(

	@field:SerializedName("awayTeam")
	val awayTeam: Int? = null,

	@field:SerializedName("homeTeam")
	val homeTeam: Int? = null
)
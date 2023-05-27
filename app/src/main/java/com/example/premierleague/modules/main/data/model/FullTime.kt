package com.example.premierleague.modules.main.data.model

import com.google.gson.annotations.SerializedName

data class FullTime(

	@field:SerializedName("awayTeam")
	val awayTeam: Int? = null,

	@field:SerializedName("homeTeam")
	val homeTeam: Int? = null
)
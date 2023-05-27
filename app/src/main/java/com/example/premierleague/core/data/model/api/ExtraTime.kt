package com.example.premierleague.core.data.model.api

import com.google.gson.annotations.SerializedName

data class ExtraTime(

	@field:SerializedName("awayTeam")
	val awayTeam: Any? = null,

	@field:SerializedName("homeTeam")
	val homeTeam: Any? = null
)
package com.example.premierleague.modules.main.data.model

import com.google.gson.annotations.SerializedName

data class ExtraTime(

	@field:SerializedName("awayTeam")
	val awayTeam: Any? = null,

	@field:SerializedName("homeTeam")
	val homeTeam: Any? = null
)
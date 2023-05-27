package com.example.premierleague.modules.main.data.model

import com.google.gson.annotations.SerializedName

data class AwayTeam(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
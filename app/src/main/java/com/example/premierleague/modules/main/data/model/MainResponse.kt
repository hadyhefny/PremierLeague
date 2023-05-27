package com.example.premierleague.modules.main.data.model

import com.google.gson.annotations.SerializedName

data class MainResponse(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("competition")
	val competition: Competition? = null,

	@field:SerializedName("filters")
	val filters: Filters? = null,

	@field:SerializedName("matches")
	val matches: List<MatchesItem?>? = null
)
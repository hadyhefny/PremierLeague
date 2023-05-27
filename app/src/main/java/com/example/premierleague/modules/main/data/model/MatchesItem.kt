package com.example.premierleague.modules.main.data.model

import com.google.gson.annotations.SerializedName

data class MatchesItem(

	@field:SerializedName("matchday")
	val matchday: Int? = null,

	@field:SerializedName("awayTeam")
	val awayTeam: AwayTeam? = null,

	@field:SerializedName("utcDate")
	val utcDate: String? = null,

	@field:SerializedName("lastUpdated")
	val lastUpdated: String? = null,

	@field:SerializedName("score")
	val score: Score? = null,

	@field:SerializedName("stage")
	val stage: String? = null,

	@field:SerializedName("odds")
	val odds: Odds? = null,

	@field:SerializedName("season")
	val season: Season? = null,

	@field:SerializedName("homeTeam")
	val homeTeam: HomeTeam? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("referees")
	val referees: List<RefereesItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("group")
	val group: Any? = null
)
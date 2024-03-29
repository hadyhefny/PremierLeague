package com.example.premierleague.core.data.model.api

import com.google.gson.annotations.SerializedName

data class RefereesItem(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("nationality")
	val nationality: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
package com.batararaja.userstory.api.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime
import java.util.*

@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val desc: String,

    @field:SerializedName("photo")
    val url : String,

    @field:SerializedName("createdAt")
    val date : String,

    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("lon")
    val lon: Double
)

package com.batararaja.userstory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryEntity (var id : String,
var photoUrl : String,
var name : String,
var description : String) :Parcelable
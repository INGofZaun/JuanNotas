package com.ad_coding.noteappcourse.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromMultimediaList(multimedia: List<String>?): String? {
        return Gson().toJson(multimedia)
    }

    @TypeConverter
    fun toMultimediaList(multimediaString: String?): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(multimediaString, type)
    }
}

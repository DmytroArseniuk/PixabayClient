package com.arsars.pixabayclient.data.source.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken


@ProvidedTypeConverter
class Converters(private val gson: Gson) {

    @TypeConverter
    fun listToJson(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}
        return gson.fromJson(value, listType)
    }
}
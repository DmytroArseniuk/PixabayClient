package com.arsars.pixabayclient.data.source

import com.arsars.pixabayclient.data.source.local.Converters
import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConverterTests {

    private val gson = Gson()
    private val converters = Converters(gson)

    @Test
    fun convert_listToJsonAndBack_Equals() {
        val list = listOf("item1", "item2", "item3")

        val toJson = converters.listToJson(list)
        val fromJson = converters.jsonToList(toJson)

        assertEquals(list, fromJson)
    }

}
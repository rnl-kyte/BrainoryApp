package com.devbytes.brainory.data.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromByteArray(bytes: ByteArray): String = String(bytes)

    @TypeConverter
    fun toByteArray(str: String): ByteArray = str.toByteArray()
}
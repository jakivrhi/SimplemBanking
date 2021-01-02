package com.example.simplymbanking.database

import androidx.room.TypeConverter
import com.example.simplymbanking.models.Account
import com.example.simplymbanking.models.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class UserTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    //for List<Transaction>
    //from Json to list
    @TypeConverter
    fun toList(json: String): List<Transaction>? {
        if (json == null) return null
        val type = object : TypeToken<List<Transaction>>() {}.type
        return Gson().fromJson(json, type)
    }

    //from list to Json
    @TypeConverter
    fun fromList(list: List<Transaction>): String? {
        if (list == null) return null
        val type = object : TypeToken<List<Transaction>>() {}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun toAccountList(json: String): List<Account>? {
        if (json == null) return null
        val type = object : TypeToken<List<Account>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromAccountList(list: List<Account>): String? {
        if (list == null) return null
        val type = object : TypeToken<List<Account>>() {}.type
        return Gson().toJson(list, type)
    }


}
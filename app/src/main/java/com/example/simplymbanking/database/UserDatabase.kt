package com.example.simplymbanking.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simplymbanking.models.User

@Database(entities = [User::class], version = 4, exportSchema = false)
@TypeConverters(UserTypeConverters::class)

abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao

}
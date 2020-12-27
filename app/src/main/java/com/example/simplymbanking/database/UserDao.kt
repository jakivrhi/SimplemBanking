package com.example.simplymbanking.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.simplymbanking.models.User
import java.util.*

//contains functions for each database operation
@Dao
interface UserDao {

    //Query , pull information out of the database
    //pull all columns for all rows in the user database table
    //returns list of users
    @Query("SELECT * FROM user")
    fun getUsers(): LiveData<List<User>>

    // pull all columns from only the row whose id matches the ID value provided
    //returns single user with given id
    @Query("SELECT * FROM user WHERE id=(:id)")
    fun getUser(id: UUID): LiveData<User?>

    @Insert
    fun registerUser(user: User)
}
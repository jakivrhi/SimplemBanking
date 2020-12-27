package com.example.simplymbanking

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.simplymbanking.database.UserDatabase
import com.example.simplymbanking.models.User
import java.util.*
import java.util.concurrent.Executors


private const val DATABASE_NAME = "user-database"

//singleton class
//logic for accessing data
class UserRepository private constructor(context: Context) {


    //creates a concrete implementation of abstract UserDatabase
    //(context, database class Room creates, name of the database file Room creates)
    private val database: UserDatabase = Room.databaseBuilder(
        context.applicationContext,
        UserDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    private val userDao = database.userDao()
    /*
    An Executor is an object that references a thread. An executor instance has a function called
    execute that accepts a block of code to run. The code you provide in the block will run on
    whatever thread the executor points to.

    Create an executor that uses a new thread (which will always be a
    background thread). Any code in the block will run on that thread, so you can perform
    your database operations there safely.
     */
    private val executor = Executors.newSingleThreadExecutor()


    fun getUsers(): LiveData<List<User>> = userDao.getUsers()

    fun getUser(id: UUID): LiveData<User?> = userDao.getUser(id)


    fun registerUser(user: User) {
        executor.execute {
            userDao.registerUser(user)
        }
    }

    //UserRepository is a singleton (only 1 will exist in app process)
    companion object {
        private var INSTANCE: UserRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = UserRepository(context)
            }
        }

        fun get(): UserRepository {
            return INSTANCE ?: throw IllegalStateException("UserRepository must be initialized")
        }
    }

}
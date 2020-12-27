package com.example.simplymbanking.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.simplymbanking.UserRepository
import com.example.simplymbanking.models.User
import java.util.*


//stores a list of User objects
class UserListViewModel : ViewModel() {

    private val userRepository = UserRepository.get()

    val userListLiveData = userRepository.getUsers()

    //stores the id of user currently displayed
    val userIdLiveData = MutableLiveData<UUID>()

    var userLiveData: LiveData<User?> = Transformations.switchMap(userIdLiveData) {
        userRepository.getUser(it)
    }

    fun loadUser(userId: UUID) {
        userIdLiveData.value = userId
    }


    fun registerUser(user: User) {
        userRepository.registerUser(user)
    }
}
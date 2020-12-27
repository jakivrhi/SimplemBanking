package com.example.simplymbanking.viewmodels

import androidx.lifecycle.ViewModel
import com.example.simplymbanking.UserRepository

class AccountListViewModel : ViewModel() {

    private val userRepository = UserRepository.get()

    //val userLiveData = userRepository.getUser()

}
package com.example.simplymbanking.viewmodels

import androidx.lifecycle.ViewModel
import com.example.simplymbanking.UserRepository

class TransactionListViewModel : ViewModel() {

    private val userRepository = UserRepository.get()

    //val userLiveData = userRepository.getUser()

}
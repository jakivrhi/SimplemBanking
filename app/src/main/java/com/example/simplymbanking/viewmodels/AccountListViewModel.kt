package com.example.simplymbanking.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplymbanking.UserRepository
import com.example.simplymbanking.ZadatakFetchr
import com.example.simplymbanking.models.AccountList

class AccountListViewModel : ViewModel() {

    private val userRepository = UserRepository.get()

    //val userLiveData = userRepository.getUser()

    val accountListItemLiveData : LiveData<AccountList> = ZadatakFetchr().fetchContents()

}
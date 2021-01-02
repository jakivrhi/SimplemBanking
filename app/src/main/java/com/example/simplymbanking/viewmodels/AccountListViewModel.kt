package com.example.simplymbanking.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplymbanking.ZadatakFetchr
import com.example.simplymbanking.models.AccountList

class AccountListViewModel : ViewModel() {

    val accountListItemLiveData : LiveData<AccountList> = ZadatakFetchr().fetchContents()

}
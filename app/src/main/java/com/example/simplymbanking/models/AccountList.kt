package com.example.simplymbanking.models

import com.google.gson.annotations.SerializedName

data class AccountList(@SerializedName("acounts") var accounts: List<Account> = listOf()) {


}
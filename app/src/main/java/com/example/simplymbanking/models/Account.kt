package com.example.simplymbanking.models

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("id") var id: String,
    @SerializedName("IBAN") var iban: String,
    @SerializedName("amount") var amount: String,
    @SerializedName("currency") var currency: String,
    @SerializedName("transactions") var transList: List<Transaction> = listOf()
) {
}
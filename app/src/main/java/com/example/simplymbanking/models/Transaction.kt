package com.example.simplymbanking.models

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("id") val id: Int,
    @SerializedName("date") var date: String,
    @SerializedName("description") var description: String,
    @SerializedName("amount") var amount: String,
    @SerializedName("type") val transactionType: String = ""
) {
}
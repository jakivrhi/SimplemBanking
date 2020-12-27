package com.example.simplymbanking.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Transaction(
    @SerializedName("id") val id: Int,
    @SerializedName("date") var date: Date = Date(),
    @SerializedName("description") var description: String,
    @SerializedName("amount") var amount: Double,
    @SerializedName("type") val transactionType: String = ""
) {
}
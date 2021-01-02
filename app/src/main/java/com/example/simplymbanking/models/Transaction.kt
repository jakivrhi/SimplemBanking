package com.example.simplymbanking.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Transaction(
    @SerializedName("id") val id: Int,
    @SerializedName("date") var date: Date,
    @SerializedName("description") var description: String,
    @SerializedName("amount") var amount: String,
    @SerializedName("type") val transactionType: String = ""
) : Comparable<Transaction>{
    override fun compareTo(other: Transaction): Int {
        val transactionDate = other.date
        val date : Date = this.date
        val dateOrder = transactionDate.compareTo(date)
        return dateOrder
    }
}
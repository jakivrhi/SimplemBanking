package com.example.simplymbanking.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var name: String = "",
    var surname: String = "",
    var pin: String = "",
    var accountList: List<Account> = listOf()
) {
}
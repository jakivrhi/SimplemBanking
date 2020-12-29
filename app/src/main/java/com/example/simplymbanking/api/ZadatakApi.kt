package com.example.simplymbanking.api

import com.example.simplymbanking.models.AccountList
import retrofit2.Call
import retrofit2.http.GET

interface ZadatakApi {

    @GET("builds/ISBD_public/Zadatak_1.json")
    fun fetchContents() : Call<AccountList>
}
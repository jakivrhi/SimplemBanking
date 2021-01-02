package com.example.simplymbanking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simplymbanking.api.ZadatakApi
import com.example.simplymbanking.models.AccountList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


private const val TAG = "ZadatakFetchr"

class ZadatakFetchr {

    private val zadatakApi: ZadatakApi

    init {
        //fix for HANDSHAKE  {mobile.asseco-see.hr supports TLS 1.0}
        var tlsSpecs: MutableList<ConnectionSpec> = Arrays.asList(ConnectionSpec.COMPATIBLE_TLS)

        val client = OkHttpClient.Builder()
            .connectionSpecs(tlsSpecs)
            .build()


        val gson : Gson = GsonBuilder().setDateFormat("dd.MM.yyyy.").create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://mobile.asseco-see.hr/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        zadatakApi = retrofit.create(ZadatakApi::class.java)
    }

    fun fetchContents(): LiveData<AccountList> {

        val responseLiveData: MutableLiveData<AccountList> = MutableLiveData()
        val call: Call<AccountList> = zadatakApi.fetchContents()

        call.enqueue(object : Callback<AccountList> {
            override fun onResponse(call: Call<AccountList>, response: Response<AccountList>) {

                val userRecieved: AccountList? = response.body()

                if (userRecieved != null) {
                    Log.d(TAG, "onResponse: " + userRecieved.accounts[0].amount)
                }
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<AccountList>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
            }

        })
        return responseLiveData
    }

}
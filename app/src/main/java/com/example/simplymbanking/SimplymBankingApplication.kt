package com.example.simplymbanking

import android.app.Application

class SimplymBankingApplication : Application() {


    //onCreate() is called by the system when application is first loaded in to memory
    // place to do one-time initialization operations
    override fun onCreate() {
        super.onCreate()
        UserRepository.initialize(this)

        //HANDSHAKE FAILED ERROR, DONT KNOW WHY
        
    }
}
package com.example.simplymbanking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplymbanking.fragments.AccountListFragment
import com.example.simplymbanking.fragments.LoginFragment
import com.example.simplymbanking.fragments.RegisterFragment
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), RegisterFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = LoginFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onDialogFinishedUserRegistered(userId: UUID) {
        val fragment = AccountListFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


}
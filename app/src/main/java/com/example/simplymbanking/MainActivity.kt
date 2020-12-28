package com.example.simplymbanking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplymbanking.fragments.AccountListFragment
import com.example.simplymbanking.fragments.LoginFragment
import com.example.simplymbanking.fragments.RegisterFragment
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), RegisterFragment.Callbacks, LoginFragment.CallbacksLogin {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = RegisterFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onDialogFinishedUserRegistered(userId: UUID) {
        val fragment = AccountListFragment.newInstance(userId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun goToFragmentLogin() {
        val fragment = LoginFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onLoginDialogFinished(userId: UUID) {
        //
    }

    override fun goToFragmentRegister() {
        val fragment = RegisterFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}
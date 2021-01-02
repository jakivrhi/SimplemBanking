package com.example.simplymbanking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplymbanking.fragments.AccountListFragment
import com.example.simplymbanking.fragments.LoginFragment
import com.example.simplymbanking.fragments.RegisterFragment
import com.example.simplymbanking.fragments.TransactionListFragment
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), RegisterFragment.Callbacks, LoginFragment.CallbacksLogin,
    AccountListFragment.Callbacks {

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
        val fragment = AccountListFragment.newInstance(userId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
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
        val fragment = AccountListFragment.newInstance(userId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun goToFragmentRegister() {
        val fragment = RegisterFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onAccountSelected(userId: UUID, accountId: String) {
        val fragment = TransactionListFragment.newInstance(userId, accountId)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onLogoutSelected() {
        val fragment = LoginFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}
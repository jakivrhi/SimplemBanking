package com.example.simplymbanking.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.simplymbanking.R
import com.example.simplymbanking.api.ZadatakApi
import com.example.simplymbanking.models.Account
import com.example.simplymbanking.models.User
import com.example.simplymbanking.viewmodels.UserListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment(), RegisterDialogFragment.OnPinEntered {

    //interface method
    override fun sendPinToFragment(pin: String) {
        user.pin = pin
    }

    interface Callbacks {
        fun onDialogFinishedUserRegistered(userId: UUID)
        fun goToFragmentLogin()
    }

    private var callbacks: Callbacks? = null
    private lateinit var user: User
    private lateinit var loginTextViewClickable: TextView
    private lateinit var registerTextViewClickable: TextView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var continueButton: Button
    private lateinit var finishRegistrationButton: Button

    private val userListViewModel: UserListViewModel by lazy {
        ViewModelProvider(this).get(UserListViewModel::class.java)
    }

    //called when a fragment is attached to an activity
    //context is activity instance hosting RegisterFragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        continueButton = view.findViewById(R.id.continue_button) as Button
        loginTextViewClickable = view.findViewById(R.id.login_text_view) as TextView
        registerTextViewClickable = view.findViewById(R.id.register_text_view) as TextView
        firstNameEditText = view.findViewById(R.id.first_name_edit_text) as EditText
        lastNameEditText = view.findViewById(R.id.last_name_edit_text) as EditText
        finishRegistrationButton = view.findViewById(R.id.finish_registration_button) as Button

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        finishRegistrationButton.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()

        user = User()

        continueButton.setOnClickListener {
            user.name = firstNameEditText.text.toString()
            user.surname = lastNameEditText.text.toString()

            showDialog()
            finishRegistrationButton.visibility = View.VISIBLE
        }

        finishRegistrationButton.setOnClickListener {
            userListViewModel.registerUser(user)
            callbacks?.onDialogFinishedUserRegistered(user.id)

            //getFromApi()
        }

        loginTextViewClickable.setOnClickListener {
            callbacks?.goToFragmentLogin()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun showDialog() {

        val dialog = RegisterDialogFragment()
        dialog.setTargetFragment(this@RegisterFragment, 1)

        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme)
        dialog.show(parentFragmentManager, "registerDialog")
    }


    //RETROFIT
    private fun getFromApi() {

        val retrofit: Retrofit = Builder()
            .baseUrl("https://mobile.asseco-see.hr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val zadatakApi: ZadatakApi = retrofit.create(ZadatakApi::class.java)

        val call: Call<List<Account>> = zadatakApi.fetchContents()

        call.enqueue(object : Callback<List<Account>> {
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {
                Log.d(TAG, "onResponse: ")

                val accounts: List<Account>? = response.body()
                if (accounts != null) {
                    user.accountList = accounts
                }
            }

            override fun onFailure(call: Call<List<Account>>, t: Throwable) {
                Log.e(TAG, "Failed to fetch", t)
            }

        })
    }

}
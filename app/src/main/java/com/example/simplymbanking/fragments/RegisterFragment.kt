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
import com.example.simplymbanking.models.User
import com.example.simplymbanking.viewmodels.AccountListViewModel
import com.example.simplymbanking.viewmodels.UserListViewModel
import java.util.*


private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment(), RegisterDialogFragment.OnPinEntered {

    //interface method
    override fun sendPinToFragment(pin: String) {
        user.pin = pin
        checkPin = pin
    }

    interface Callbacks {
        fun onDialogFinishedUserRegistered(userId: UUID)
        fun goToFragmentLogin()
    }

    //NOVO
    private lateinit var accountListViewModel: AccountListViewModel

    private var checkPin = ""
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountListViewModel = ViewModelProvider(this).get(AccountListViewModel::class.java)
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
            //getFromApi()

            user.accountList = accountListViewModel.accountListItemLiveData.value!!.accounts
            userListViewModel.registerUser(user)
            Log.d(TAG, "onStart: " + user.accountList[0].amount + " DOBRO JE")
            callbacks?.onDialogFinishedUserRegistered(user.id)
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

        /*
        val zadatakLiveData: LiveData<AccountList> = ZadatakFetchr().fetchContents()
        zadatakLiveData.observe(
            this, androidx.lifecycle.Observer { responseAccountList ->
                Log.d(TAG, "getFromApi: $responseAccountList")
            }
        )

         */

        accountListViewModel = ViewModelProvider(this).get(AccountListViewModel::class.java)
        /*
        //fix for HANDSHAKE  mobile.asseco-see.hr supports TLS 1.0
        var tlsSpecs: MutableList<ConnectionSpec> = Arrays.asList(ConnectionSpec.COMPATIBLE_TLS)

        val client = OkHttpClient.Builder()
            .connectionSpecs(tlsSpecs)
            .build()

        val retrofit: Retrofit = Builder()
            .baseUrl("https://mobile.asseco-see.hr/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


        val zadatakApi: ZadatakApi = retrofit.create(ZadatakApi::class.java)



        val call: Call<AccountList> = zadatakApi.fetchContents()

        call.enqueue(object : Callback<AccountList> {
            override fun onResponse(call: Call<AccountList>, response: Response<AccountList>) {
                Log.d(TAG, "onResponse: " +response.code())

                val userRecieved: AccountList? = response.body()

                if (userRecieved != null) {
                    user.accountList = userRecieved.accounts
                    //Log.d(TAG, "onResponse: " + user.accountList[0].amount.toString())
                }
            }

            override fun onFailure(call: Call<AccountList>, t: Throwable) {
                Log.e(TAG, "Failed to fetch", t)
            }

        })

         */
    }

}
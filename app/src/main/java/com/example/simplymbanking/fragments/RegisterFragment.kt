package com.example.simplymbanking.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
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
        checkPin.value = pin
    }

    interface Callbacks {
        fun onDialogFinishedUserRegistered(userId: UUID)
        fun goToFragmentLogin()
    }

    //NOVO
    private lateinit var accountListViewModel: AccountListViewModel

    private var checkPin: MutableLiveData<String> = MutableLiveData<String>()
    private var callbacks: Callbacks? = null
    private lateinit var user: User
    private lateinit var loginTextViewClickable: TextView
    private lateinit var registerTextViewClickable: TextView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var continueButton: Button
    private lateinit var finishRegistrationButton: Button
    private lateinit var cancelRegistrationButton: Button
    private lateinit var underlineButton: Button
    private lateinit var questionWelcomeTextView: TextView
    private lateinit var userNameSurnameWelcomeTextView: TextView

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
        checkPin.value = ""

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
        cancelRegistrationButton = view.findViewById(R.id.cancel_registration_button) as Button
        underlineButton = view.findViewById(R.id.underline_button) as Button
        questionWelcomeTextView = view.findViewById(R.id.welcome_question_text_view) as TextView
        userNameSurnameWelcomeTextView =
            view.findViewById(R.id.welcome_user_name_surname_text_view) as TextView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        finishRegistrationButton.visibility = View.INVISIBLE

        checkPin.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (checkPin.value?.length!! >= 4) {
                finishRegistrationButton.visibility = View.VISIBLE
                questionWelcomeTextView.visibility = View.VISIBLE
                cancelRegistrationButton.visibility = View.VISIBLE
                userNameSurnameWelcomeTextView.visibility = View.VISIBLE
                userNameSurnameWelcomeTextView.text = user.name + " " + user.surname
                questionWelcomeTextView.text =
                    "Register new user?"
                continueButton.visibility = View.INVISIBLE
                loginTextViewClickable.visibility = View.INVISIBLE
                registerTextViewClickable.visibility = View.INVISIBLE
                firstNameEditText.visibility = View.INVISIBLE
                lastNameEditText.visibility = View.INVISIBLE
                underlineButton.visibility = View.INVISIBLE
            }
        })
    }

    override fun onStart() {
        super.onStart()

        user = User()

        continueButton.setOnClickListener {
            if(firstNameEditText.text.toString() == "" || lastNameEditText.text.toString() == ""){
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }else{
                user.name = firstNameEditText.text.toString()
                user.surname = lastNameEditText.text.toString()

                showDialog()
            }
        }

        finishRegistrationButton.setOnClickListener {
            //getFromApi()

            user.accountList = accountListViewModel.accountListItemLiveData.value!!.accounts
            userListViewModel.registerUser(user)
            Log.d(TAG, "onStart: " + user.accountList[0].amount + " DOBRO JE")
            callbacks?.onDialogFinishedUserRegistered(user.id)
        }

        cancelRegistrationButton.setOnClickListener {
            callbacks?.goToFragmentLogin()
        }

        loginTextViewClickable.setOnClickListener {
            callbacks?.goToFragmentLogin()
        }

        //fix focus
        firstNameEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                firstNameEditText.clearFocus()
            }
            false
        })

        lastNameEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                lastNameEditText.clearFocus()
            }
            false
        })

        firstNameEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                // code to execute when EditText loses focus
                v.hideKeyboard()
            }
        }

        lastNameEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                // code to execute when EditText loses focus
                v.hideKeyboard()
            }
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

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
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
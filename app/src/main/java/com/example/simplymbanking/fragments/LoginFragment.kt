package com.example.simplymbanking.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.simplymbanking.R
import com.example.simplymbanking.models.User
import com.example.simplymbanking.viewmodels.UserListViewModel
import com.google.gson.Gson
import java.util.*

private const val USER_KEY = "user key"
private const val NAME_KEY = "name key"
private const val SURNAME_KEY = "surname key"

class LoginFragment : Fragment(), LoginDialogFragment.LoginPinEntered,
    RegisteredUsersListFragment.ChosenRegisteredUser {

    //interface method
    override fun sendLoginPinToFragment(pin: String) {
        checkPin.value = pin
        if(pin != user.pin){
            Toast.makeText(context, "Wrong PIN", Toast.LENGTH_SHORT).show()
        }
    }

    override fun sendChosenRegisteredUser(id: UUID) {
        userListViewModel.loadUser(id)
        checkPin.value = ""
    }

    interface CallbacksLogin {
        fun onLoginDialogFinished(userId: UUID)
        fun goToFragmentRegister()
    }

    private var callbacksLogin: CallbacksLogin? = null
    private var checkPin: MutableLiveData<String> = MutableLiveData<String>()
    private lateinit var user: User
    private lateinit var loginTextViewClickable: TextView
    private lateinit var registerTextViewClickable: TextView
    private lateinit var loginButton: Button
    private lateinit var changeUserTextViewClickable: TextView
    private lateinit var userNameLoginTextView: TextView
    private lateinit var underlineButton: Button
    private lateinit var userSurnameLoginTextView: TextView
    private lateinit var welcomeBackTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var finishLoginButton: Button
    private lateinit var lottieAnim: LottieAnimationView
    private lateinit var staySafeTextView: TextView

    private lateinit var userNamePref: String
    private lateinit var userSurnamePref: String

    private lateinit var userId: UUID

    private val userListViewModel: UserListViewModel by lazy {
        ViewModelProvider(this).get(UserListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacksLogin = context as CallbacksLogin?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPin.value = ""
        userNamePref = ""
        userSurnamePref = ""
        userId = UUID.randomUUID()
        loadUserPreferences()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        welcomeBackTextView = view.findViewById(R.id.welcome_back_text_view) as TextView
        userNameTextView = view.findViewById(R.id.user_name_text_view) as TextView
        finishLoginButton = view.findViewById(R.id.finish_login_button) as Button
        loginTextViewClickable = view.findViewById(R.id.login_text_view) as TextView
        registerTextViewClickable = view.findViewById(R.id.register_text_view) as TextView
        loginButton = view.findViewById(R.id.login_button) as Button
        underlineButton = view.findViewById(R.id.underline_button) as Button
        changeUserTextViewClickable = view.findViewById(R.id.change_user_text_view) as TextView
        userNameLoginTextView = view.findViewById(R.id.user_name_login_text_view) as TextView
        userSurnameLoginTextView = view.findViewById(R.id.user_surname_login_text_view) as TextView
        lottieAnim = view.findViewById(R.id.lottie_animation) as LottieAnimationView
        staySafeTextView = view.findViewById(R.id.stay_safe_text_view) as TextView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userListViewModel.userLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer { user ->
                user?.let {
                    this.user = user
                    updateUI(user)
                }
            }
        )
        checkPin.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (checkPin.value == user.pin && checkPin.value?.length!! >= 4) {
                welcomeBackTextView.visibility = View.VISIBLE
                userNameTextView.visibility = View.VISIBLE
                finishLoginButton.visibility = View.VISIBLE
                lottieAnim.visibility = View.VISIBLE
                staySafeTextView.visibility = View.VISIBLE
                loginTextViewClickable.visibility = View.INVISIBLE
                registerTextViewClickable.visibility = View.INVISIBLE
                loginButton.visibility = View.INVISIBLE
                changeUserTextViewClickable.visibility = View.INVISIBLE
                userNameLoginTextView.visibility = View.INVISIBLE
                userSurnameLoginTextView.visibility = View.INVISIBLE
                underlineButton.visibility = View.INVISIBLE
                userNameTextView.text = user.name
            }
        })
    }

    override fun onStart() {
        super.onStart()
        user = User()

        loadUserPreferences()
        user.name = userNamePref
        user.surname = userSurnamePref

        userNameLoginTextView.text = user.name
        userSurnameLoginTextView.text = user.surname

        loginButton.setOnClickListener {
            userListViewModel.loadUser(userId)
            user.name = userNameLoginTextView.text.toString()
            user.surname = userSurnameLoginTextView.text.toString()

            showDialog()
        }

        finishLoginButton.setOnClickListener {
            userListViewModel.loadUser(user.id)
            /*
            I SALJI GA DALJE U AccountListFragment()
            ili, spremi u shared pref?
             */
            callbacksLogin?.onLoginDialogFinished(user.id)
        }

        registerTextViewClickable.setOnClickListener {
            callbacksLogin?.goToFragmentRegister()
        }

        changeUserTextViewClickable.setOnClickListener {
            showRegisteredUsersDialog()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacksLogin = null
    }

    private fun updateUI(user: User) {
        userNameLoginTextView.text = user.name
        userSurnameLoginTextView.text = user.surname
        userNamePref = user.name
        userSurnamePref = user.surname
        saveUserPreferences()
        //spremiti user.id i FALSE  u shared pref
        //nakon unosa ispravnog PIN-a , shared pref TRUE
    }

    private fun showDialog() {
        val dialog = LoginDialogFragment()
        dialog.setTargetFragment(this@LoginFragment, 1)

        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme)
        dialog.show(parentFragmentManager, "loginDialog")
    }

    private fun showRegisteredUsersDialog() {
        val dialog = RegisteredUsersListFragment()
        dialog.setTargetFragment(this@LoginFragment, 1)

        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme)
        dialog.show(parentFragmentManager, "registerDialog")
    }

    private fun saveUserPreferences() {
        val sharedPreferences = activity?.getSharedPreferences(
            "shared preferences user",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences!!.edit()

        editor.putString(NAME_KEY, userNamePref).putString(SURNAME_KEY, userSurnamePref)

        val gson = Gson()
        val json: String = gson.toJson(userId)
        editor.putString(USER_KEY, json)

        editor.apply()
    }

    private fun loadUserPreferences() {
        val sharedPreferences =
            activity?.getSharedPreferences("shared preferences user", Context.MODE_PRIVATE)

        userNamePref = sharedPreferences?.getString(NAME_KEY, "default").toString()
        userSurnamePref = sharedPreferences?.getString(SURNAME_KEY, "other").toString()

        val gson: Gson = Gson()
        val json: String = sharedPreferences?.getString(USER_KEY, "def").toString()

        //error? try
        try {
            userId = gson.fromJson(json, UUID::class.java)
        }catch (e : Exception){
            //
        }
    }

}
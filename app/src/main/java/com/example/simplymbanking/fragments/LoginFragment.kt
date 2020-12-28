package com.example.simplymbanking.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.simplymbanking.R
import com.example.simplymbanking.models.User
import com.example.simplymbanking.viewmodels.UserListViewModel
import java.util.*

private const val USER_KEY = "user key"

class LoginFragment : Fragment(), LoginDialogFragment.LoginPinEntered,
    RegisteredUsersListFragment.ChosenRegisteredUser {

    //interface function
    override fun sendLoginPinToFragment(pin: String) {
        user.pin = pin
    }

    override fun sendChosenRegisteredUser(id: UUID) {
        chosenId = id
        userListViewModel.loadUser(chosenId)
    }

    interface CallbacksLogin {
        fun onLoginDialogFinished(userId: UUID)
        fun goToFragmentRegister()
    }

    private var chosenId: UUID = UUID.randomUUID()
    private var callbacksLogin: CallbacksLogin? = null
    private lateinit var user: User
    private lateinit var loginTextViewClickable: TextView
    private lateinit var registerTextViewClickable: TextView
    private lateinit var loginButton: Button
    private lateinit var changeUserTextViewClickable: TextView
    private lateinit var userNameLoginTextView: TextView
    private lateinit var userSurnameLoginTextView: TextView

    private val userListViewModel: UserListViewModel by lazy {
        ViewModelProvider(this).get(UserListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacksLogin = context as CallbacksLogin?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        loginTextViewClickable = view.findViewById(R.id.login_text_view) as TextView
        registerTextViewClickable = view.findViewById(R.id.register_text_view) as TextView
        loginButton = view.findViewById(R.id.login_button) as Button
        changeUserTextViewClickable = view.findViewById(R.id.change_user_text_view) as TextView
        userNameLoginTextView = view.findViewById(R.id.user_name_login_text_view)
        userSurnameLoginTextView = view.findViewById(R.id.user_surname_login_text_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userListViewModel.userLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer { user ->
                user?.let {
                    this.user = user
                    updateUI(user)
                    saveUserPreferences(user.id)
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        user = User()

        loginButton.setOnClickListener {
            user.name = userNameLoginTextView.text.toString()
            user.surname = userSurnameLoginTextView.text.toString()

            showDialog()
        }
        /*
        finishLoginButton.setOnClickListener {
            userListViewModel.loadUser(user.id)

            I SALJI GA DALJE U AccountListFragment()
            ili, spremi u shared pref?
        }
         */

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
        //spremiti user.id i FALSE  u shared pref
        //nakon unosa ispravnog PIN-a , shared pref TRUE
    }


    private fun showDialog() {

        /*
        LoginDialogFragment().apply {
            setTargetFragment(this@LoginFragment, 1)
        }

         */


        val dialog = LoginDialogFragment()
        dialog.setTargetFragment(this@LoginFragment, 1)

        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme)
        dialog.show(parentFragmentManager, "loginDialog")

        /*
        val dialog = RegisterDialogFragment()
        dialog.setTargetFragment(this@RegisterFragment, 1)

        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme)
        dialog.show(parentFragmentManager, "registerDialog")
         */
    }

    private fun showRegisteredUsersDialog() {
        val dialog = RegisteredUsersListFragment()
        dialog.setTargetFragment(this@LoginFragment, 1)

        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme)
        dialog.show(parentFragmentManager, "registerDialog")
    }

    //
    private fun saveUserPreferences(userId: UUID) {
        val sharedPreferences = activity?.getSharedPreferences(
            "shared preferences user",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences!!.edit()

        editor.putString(USER_KEY, userId.toString())
        editor.apply()
    }

}
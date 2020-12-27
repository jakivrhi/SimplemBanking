package com.example.simplymbanking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.simplymbanking.R
import com.example.simplymbanking.models.User
import com.example.simplymbanking.viewmodels.UserListViewModel

class LoginFragment : Fragment() {

    private lateinit var user: User
    private lateinit var loginTextViewClickable: TextView
    private lateinit var registerTextViewClickable: TextView
    private lateinit var userNameSurnameTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var changeUserTextViewClickable: TextView

    private val userListViewModel: UserListViewModel by lazy {
        ViewModelProvider(this).get(UserListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = User()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        loginTextViewClickable = view.findViewById(R.id.login_text_view) as TextView
        registerTextViewClickable = view.findViewById(R.id.register_text_view) as TextView
        userNameSurnameTextView = view.findViewById(R.id.user_name_surname_text_view) as TextView
        loginButton = view.findViewById(R.id.login_button) as Button
        changeUserTextViewClickable = view.findViewById(R.id.change_user_text_view) as TextView

        return view
    }

    override fun onStart() {
        super.onStart()

        loginButton.setOnClickListener {
            showDialog()
        }
    }


    private fun showDialog() {
        LoginDialogFragment().apply {
            setTargetFragment(this@LoginFragment, 1)
        }

        /*
        val dialog = LoginDialogFragment()
        dialog.setTargetFragment(this@LoginFragment, 1)

        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme)
        dialog.show(parentFragmentManager, "loginDialog")

         */
    }
}
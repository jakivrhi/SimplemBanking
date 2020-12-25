package com.example.simplymbanking.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.simplymbanking.R
import com.example.simplymbanking.models.User
import java.util.*

class RegisterFragment : Fragment(), RegisterDialogFragment.Callbacks {

    //for hosting activities
    interface Callbacks {
        fun onUserRegistered(userId: UUID)
        fun onDialogOpened()
    }

    private var callbacks: Callbacks? = null

    private lateinit var user: User
    private lateinit var loginTextViewClickable: TextView
    private lateinit var registerTextViewClickable: TextView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var continueButton: Button
    private lateinit var dialog: RegisterDialogFragment

    //called when a fragment is attached to an activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as RegisterFragment.Callbacks?
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

        return view
    }


    override fun onStart() {
        super.onStart()
        user = User()

        continueButton.setOnClickListener {
            user.name = firstNameEditText.text.toString()
            user.surname = lastNameEditText.text.toString()

            showDialog()

            if(user.pin.length >= 4 || user.pin.length <= 6){
                callbacks?.onUserRegistered(user.id)
            }else{
                Toast.makeText(context, "Error, please try again", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun showDialog() {
        dialog = RegisterDialogFragment()
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme)
        dialog.show(childFragmentManager, "registerDialog")
    }

    override fun onDialogFinished(pin: String) {
        user.pin = pin
    }
}
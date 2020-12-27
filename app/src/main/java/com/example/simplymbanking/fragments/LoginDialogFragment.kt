package com.example.simplymbanking.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.example.simplymbanking.R

private const val TAG = "LoginDialogFragment"

class LoginDialogFragment : DialogFragment() {

    var pin: String = ""
    private lateinit var button0: Button
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var button6: Button
    private lateinit var button7: Button
    private lateinit var button8: Button
    private lateinit var button9: Button
    private lateinit var pinEditText: EditText
    private lateinit var deleteButton: ImageButton
    private lateinit var loginButton: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pin_register_dialog, container, false)
        button0 = view.findViewById(R.id.button_0) as Button
        button1 = view.findViewById(R.id.button_1) as Button
        button2 = view.findViewById(R.id.button_2) as Button
        button3 = view.findViewById(R.id.button_3) as Button
        button4 = view.findViewById(R.id.button_4) as Button
        button5 = view.findViewById(R.id.button_5) as Button
        button6 = view.findViewById(R.id.button_6) as Button
        button7 = view.findViewById(R.id.button_7) as Button
        button8 = view.findViewById(R.id.button_8) as Button
        button9 = view.findViewById(R.id.button_9) as Button
        pinEditText = view.findViewById(R.id.pin_register_edit_text) as EditText
        deleteButton = view.findViewById(R.id.keyboard_delete_number_image_button) as ImageButton
        loginButton = view.findViewById(R.id.register_button) as Button

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.text = "LOGIN"
    }

    override fun onStart() {
        super.onStart()
        enableDisableButton()

        button0.setOnClickListener {
            clickToNumber("button0")
        }
        button1.setOnClickListener {
            clickToNumber("button1")
        }
        button2.setOnClickListener {
            clickToNumber("button2")
        }
        button3.setOnClickListener {
            clickToNumber("button3")
        }
        button4.setOnClickListener {
            clickToNumber("button4")
        }
        button5.setOnClickListener {
            clickToNumber("button5")
        }
        button6.setOnClickListener {
            clickToNumber("button6")
        }
        button7.setOnClickListener {
            clickToNumber("button7")
        }
        button8.setOnClickListener {
            clickToNumber("button8")
        }
        button9.setOnClickListener {
            clickToNumber("button9")
        }

        deleteButton.setOnClickListener {
            deleteLastNumber()
        }

        loginButton.setOnClickListener {
            pin = this.pinEditText.text.toString()


            this.dismiss()
        }
    }

    private fun validatePin(pin: Int): Boolean {
        return !(pin < 4 || pin > 6)
    }

    private fun clickToNumber(button: String) {
        var numberString = button.replace("button", "")
        pinEditText.append(numberString)
    }

    private fun deleteLastNumber() {
        var text: String = pinEditText.text.toString()
        if (text.isNotEmpty()) {
            text = text.removeRange(text.length - 1, text.length)
        }

        pinEditText.setText(text)
    }

    private fun enableDisableButton() {
        loginButton.isEnabled = false
        deleteButton.isEnabled = false
        pinEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                loginButton.isEnabled = s.toString().trim { it <= ' ' }.length >= 4
                button0.isEnabled = s.toString().trim { it <= ' ' }.length < 6
                button1.isEnabled = s.toString().trim { it <= ' ' }.length < 6
                button2.isEnabled = s.toString().trim { it <= ' ' }.length < 6
                button3.isEnabled = s.toString().trim { it <= ' ' }.length < 6
                button4.isEnabled = s.toString().trim { it <= ' ' }.length < 6
                button5.isEnabled = s.toString().trim { it <= ' ' }.length < 6
                button6.isEnabled = s.toString().trim { it <= ' ' }.length < 6
                button7.isEnabled = s.toString().trim { it <= ' ' }.length < 6
                button8.isEnabled = s.toString().trim { it <= ' ' }.length < 6
                button9.isEnabled = s.toString().trim { it <= ' ' }.length < 6
                deleteButton.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }
}
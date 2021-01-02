package com.example.simplymbanking.fragments

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
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
        if (pin != user.pin) {
            notifyUserToast("Wrong PIN")
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

    private lateinit var fingerprintButton: Button

    private val userListViewModel: UserListViewModel by lazy {
        ViewModelProvider(this).get(UserListViewModel::class.java)
    }

    //BIOMETRICS variables
    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUserToast("Authentication error $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUserToast("Authentication success")
                    userListViewModel.loadUser(user.id)
                    callbacksLogin?.onLoginDialogFinished(user.id)
                }

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

        checkBiometricSupport()

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
        fingerprintButton = view.findViewById(R.id.finish_login_fingerprint_button) as Button
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
        userListViewModel.loadUser(userId)
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
                fingerprintButton.visibility = View.INVISIBLE
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
            if (userNameLoginTextView.text.toString() == "Choose or create a") {
                Toast.makeText(context, "Please register of choose a user", Toast.LENGTH_SHORT)
                    .show()
            } else {
                user.name = userNameLoginTextView.text.toString()
                user.surname = userSurnameLoginTextView.text.toString()

                showDialog()
            }
        }

        finishLoginButton.setOnClickListener {
            userListViewModel.loadUser(user.id)
            callbacksLogin?.onLoginDialogFinished(user.id)
        }

        registerTextViewClickable.setOnClickListener {
            callbacksLogin?.goToFragmentRegister()
        }

        changeUserTextViewClickable.setOnClickListener {
            showRegisteredUsersDialog()
        }


        fingerprintButton.setOnClickListener {
            if (userNameLoginTextView.text.toString() == "Choose or create a") {
                Toast.makeText(context, "Please register of choose a user", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    val biometricPrompt = BiometricPrompt.Builder(context)
                        .setTitle("Simply mBanking login authentication")
                        .setSubtitle("Authentication is required")
                        .setDescription("Secure and fast login")
                        .setNegativeButton(
                            "Cancel",
                            context!!.mainExecutor,
                            DialogInterface.OnClickListener { dialog, which ->
                                notifyUserToast("Authentication cancelled")
                            }).build()
                    biometricPrompt.authenticate(
                        getCancellationSignal(),
                        context!!.mainExecutor,
                        authenticationCallback
                    )
                } else {
                    notifyUserToast("Too low version of Android")
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        userListViewModel.loadUser(userId)
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
        userId = user.id
        saveUserPreferences()
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

        userNamePref = sharedPreferences?.getString(NAME_KEY, "Choose or create a").toString()
        userSurnamePref = sharedPreferences?.getString(SURNAME_KEY, "user").toString()

        val gson: Gson = Gson()
        val json: String = sharedPreferences?.getString(USER_KEY, "def").toString()

        //error? try
        try {
            userId = gson.fromJson(json, UUID::class.java)
        } catch (e: Exception) {
            //
        }
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        //auth canceled by user
        cancellationSignal?.setOnCancelListener {
            notifyUserToast("Authentication was cancelled by user")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = context?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure) {
            notifyUserToast("Please enable fingerprint in your phone settings")
            return false
        }
        //check if user biometrics permission is enabled
        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUserToast("Fingerprint auth is not enabled")
            return false
        }

        if (context!!.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            return true
        } else {
            return true
        }
    }

    private fun notifyUserToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}
package com.example.simplymbanking.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplymbanking.R
import com.example.simplymbanking.models.Account
import com.example.simplymbanking.models.User
import com.example.simplymbanking.viewmodels.UserListViewModel
import java.util.*

private const val ARG_USER_ID = "user_id"

class AccountListFragment : Fragment() {

    interface Callbacks {
        fun onAccountSelected(userId: UUID, accountId: String)
        fun onLogoutSelected()
    }
    private var callbacks: Callbacks? = null

    private lateinit var user: User

    private lateinit var accountsRecyclerView: RecyclerView
    private var adapter: AccountAdapter? = AccountAdapter(emptyList())

    private lateinit var userFirstLastName: TextView
    private lateinit var helloThereTextView : TextView
    private lateinit var logoutImageButton: ImageButton

    private val userListViewModel: UserListViewModel by lazy {
        ViewModelProvider(this).get(UserListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //System.exit(0)
                exitDialog(view!!)
            }
        })
        user = User()

        val userId: UUID = arguments?.getSerializable(ARG_USER_ID) as UUID
        userListViewModel.loadUser(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_list, container, false)

        userFirstLastName = view.findViewById(R.id.user_first_last_name_text_view) as TextView
        logoutImageButton = view.findViewById(R.id.logout_image_button) as ImageButton
        helloThereTextView = view.findViewById(R.id.hello_there_text_view) as TextView
        accountsRecyclerView = view.findViewById(R.id.accounts_recycler_view) as RecyclerView
        accountsRecyclerView.layoutManager = LinearLayoutManager(context)
        accountsRecyclerView.adapter = adapter

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

        greetBasedOnTime(helloThereTextView)
    }

    override fun onStart() {
        super.onStart()
        logoutImageButton.setOnClickListener {
            showAreYoUSureDialog(it)
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(userId: UUID): AccountListFragment {
            val args = Bundle().apply {
                putSerializable(ARG_USER_ID, userId)
            }
            return AccountListFragment().apply {
                arguments = args
            }
        }
    }

    private inner class AccountHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var account: Account

        private val accountIbanTextView: TextView =
            itemView.findViewById(R.id.account_iban_text_view)
        private val accountBalanceAmountTextView: TextView =
            itemView.findViewById(R.id.account_balance_amount)
        private val accountBalanceCurrency: TextView =
            itemView.findViewById(R.id.balance_amount_currency_text_view)

        fun bind(account: Account) {
            this.account = account
            accountIbanTextView.text = account.iban
            accountBalanceAmountTextView.text = account.amount
            accountBalanceCurrency.text = account.currency
        }

        init {
            itemView.setOnClickListener {
                if (itemView.isPressed) {
                    accountIbanTextView.setTextColor(Color.WHITE)
                    callbacks?.onAccountSelected(user.id, account.id)
                }
            }
        }

    }

    private inner class AccountAdapter(var accounts: List<Account>) :
        RecyclerView.Adapter<AccountHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {

            val view = layoutInflater.inflate(R.layout.list_item_account, parent, false)
            return AccountHolder(view)
        }

        override fun onBindViewHolder(holder: AccountHolder, position: Int) {
            val account = accounts[position]
            holder.bind(account)
        }

        override fun getItemCount(): Int {
            return accounts.size
        }

    }

    private fun updateUI(user: User) {
        adapter = AccountAdapter(user.accountList)
        accountsRecyclerView.adapter = adapter

        userFirstLastName.text = user.name + " " + user.surname
        Log.d("TAG", "updateUI: " + user.accountList[0].amount)
    }

    private fun showAreYoUSureDialog(view: View) {
        val dialog = Dialog(view.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.are_you_sure_logout_dialog)
        val btnYes = dialog.findViewById(R.id.yes_logout_button) as Button
        val btnNo = dialog.findViewById(R.id.no_logout_button) as Button
        btnYes.setOnClickListener {
            callbacks?.onLogoutSelected()
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        //size of dialog
        dialog.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun exitDialog(view: View) {
        val dialog = Dialog(view.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.are_you_sure_logout_dialog)
        val btnYes = dialog.findViewById(R.id.yes_logout_button) as Button
        val btnNo = dialog.findViewById(R.id.no_logout_button) as Button
        var toolbar = dialog.findViewById(R.id.toolbar_dialog) as Toolbar
        toolbar.title = "Are you sure you want to exit?"
        btnYes.setOnClickListener {
            callbacks?.onLogoutSelected()
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        //size of dialog
        dialog.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun greetBasedOnTime(textView: TextView){
        //Get the time of day
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        val hour = cal[Calendar.HOUR_OF_DAY]

        var greeting: String = ""

        greeting = when (hour) {
            in 12..16 -> {
                "Good Afternoon";
            }
            in 17..20 -> {
                "Good Evening";
            }
            in 21..23 -> {
                "Good Night";
            }
            else -> {
                "Good Morning";
            }
        }
        textView.text = greeting
    }
}
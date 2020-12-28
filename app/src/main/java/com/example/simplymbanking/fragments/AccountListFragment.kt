package com.example.simplymbanking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.simplymbanking.R
import com.example.simplymbanking.models.Account
import com.example.simplymbanking.models.User
import com.example.simplymbanking.viewmodels.UserListViewModel
import java.util.*

private const val ARG_USER_ID = "user_id"

class AccountListFragment : Fragment() {

    private lateinit var user: User
    private var pinPin: String = ""
    private var adapter: AccountAdapter? = null
    private lateinit var accountsRecyclerView: RecyclerView
    private lateinit var userFirstLastName: TextView

    private val userListViewModel: UserListViewModel by lazy {
        ViewModelProvider(this).get(UserListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = User()

        val userId: UUID = arguments?.getSerializable(ARG_USER_ID) as UUID

        //LOAD USER FROM DB
        userListViewModel.loadUser(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_list, container, false)
        accountsRecyclerView = view.findViewById(R.id.accounts_recycler_view) as RecyclerView
        userFirstLastName = view.findViewById(R.id.user_first_last_name_text_view) as TextView

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userListViewModel.userLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer { user ->
                user?.let {
                    this.user = user
                    updateUI(user.accountList)
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()



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
        val accountIbanTextView: TextView = itemView.findViewById(R.id.account_iban_text_view)
        val accountBalanceAmountTextView: TextView =
            itemView.findViewById(R.id.account_balance_amount)
        val accountBalanceCurrency: TextView =
            itemView.findViewById(R.id.balance_amount_currency_text_view)

    }

    private inner class AccountAdapter(var accounts: List<Account>) :
        RecyclerView.Adapter<AccountHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
            val view = layoutInflater.inflate(R.layout.list_item_account, parent, false)
            return AccountHolder(view)
        }

        override fun onBindViewHolder(holder: AccountHolder, position: Int) {
            val account = accounts[position]
            holder.apply {
                accountIbanTextView.text = account.iban
                accountBalanceAmountTextView.text = account.amount.toString()
            }
        }

        override fun getItemCount(): Int {
            return accounts.size
        }

    }

    private fun updateUI(accountList: List<Account>) {
        /*
        val accounts = accountListViewModel.accounts
        adapter = AccountAdapter(accounts)
        accountsRecyclerView.adapter = adapter

         */
        userFirstLastName.text = user.name + " " + user.surname
    }
}
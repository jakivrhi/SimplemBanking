package com.example.simplymbanking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.simplymbanking.R
import com.example.simplymbanking.models.Account

class AccountListFragment : Fragment() {

    private var pinPin: String = ""
    private var adapter : AccountAdapter? = null
    private lateinit var accountsRecyclerView: RecyclerView
    private lateinit var userFirstLastName: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onStart() {
        super.onStart()

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

    private fun updateUI(){
        /*
        val accounts = accountListViewModel.accounts
        adapter = AccountAdapter(accounts)
        accountsRecyclerView.adapter = adapter

         */
    }
}
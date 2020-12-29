package com.example.simplymbanking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplymbanking.R
import com.example.simplymbanking.models.Transaction
import com.example.simplymbanking.models.User
import com.example.simplymbanking.viewmodels.UserListViewModel
import java.util.*

private const val TAG = "TransactionListFragment"
private const val ARG_ACC_ID = "account_id"
private const val ARG_USER_ID = "user_id"

class TransactionListFragment : Fragment() {

    private lateinit var transactionRecyclerView: RecyclerView
    private var adapter: TransactionAdapter? = null

    private lateinit var accountIbanTextView: TextView
    private lateinit var accountBalanceAmountTextView: TextView
    private lateinit var accountAmountCurrencyTextView: TextView
    private lateinit var userNameSurnameTextView: TextView

    private val userListViewModel: UserListViewModel by lazy {
        ViewModelProvider(this).get(UserListViewModel::class.java)
    }

    private var accountId = ""
    private lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = User()

        accountId = arguments?.getString(ARG_ACC_ID) as String
        val userId: UUID = arguments?.getSerializable(ARG_USER_ID) as UUID
        userListViewModel.loadUser(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)
        transactionRecyclerView = view.findViewById(R.id.transaction_recycler_view) as RecyclerView
        transactionRecyclerView.layoutManager = LinearLayoutManager(context)
        userNameSurnameTextView = view.findViewById(R.id.user_name_surname_text_view) as TextView
        accountIbanTextView = view.findViewById(R.id.account_iban_text_view) as TextView
        accountBalanceAmountTextView = view.findViewById(R.id.account_balance_amount) as TextView
        accountAmountCurrencyTextView =
            view.findViewById(R.id.balance_amount_currency_text_view) as TextView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userListViewModel.userLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer { user ->
                user?.let {
                    this.user = user
                    updateUI(user, accountId)
                }
            }
        )
    }

    private inner class TransactionHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transactionDateTextView: TextView =
            itemView.findViewById(R.id.transaction_date_text_view)
        val transactionTypeTextView: TextView =
            itemView.findViewById(R.id.transaction_type_text_view)
        val transactionDescriptionTextView: TextView =
            itemView.findViewById(R.id.transaction_description_text_view)
        val transactionAmountTextView: TextView =
            itemView.findViewById(R.id.transaction_amount_text_view)
    }

    private inner class TransactionAdapter(var transactions: List<Transaction>) :
        RecyclerView.Adapter<TransactionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
            val view = layoutInflater.inflate(R.layout.list_item_transaction, parent, false)
            return TransactionHolder(view)
        }

        override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
            val transaction = transactions[position]
            holder.apply {
                transactionDateTextView.text = transaction.date
                transactionTypeTextView.text = transaction.transactionType
                transactionDescriptionTextView.text = transaction.description
                transactionAmountTextView.text = transaction.amount
            }
        }

        override fun getItemCount(): Int {
            return transactions.size
        }

    }

    private fun updateUI(user: User, accountId: String) {
        accountIbanTextView.text = user.accountList[accountId.toInt() - 1].iban
        accountBalanceAmountTextView.text = user.accountList[accountId.toInt() - 1].amount
        accountAmountCurrencyTextView.text = user.accountList[accountId.toInt() - 1].currency

        adapter = TransactionAdapter(user.accountList[accountId.toInt() - 1].transList)
        transactionRecyclerView.adapter = adapter
        userNameSurnameTextView.text = user.name + " " + user.surname
    }

    companion object {
        fun newInstance(userId: UUID, accountId: String): TransactionListFragment {
            val args = Bundle().apply {
                putString(ARG_ACC_ID, accountId)
                putSerializable(ARG_USER_ID, userId)
            }
            return TransactionListFragment().apply {
                arguments = args
            }
        }
    }
}
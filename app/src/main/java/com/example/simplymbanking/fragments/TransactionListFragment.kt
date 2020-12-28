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
import com.example.simplymbanking.viewmodels.TransactionListViewModel
import java.text.SimpleDateFormat

private const val TAG = "TransactionListFragment"

class TransactionListFragment : Fragment() {

    private lateinit var transactionRecyclerView: RecyclerView
    private var adapter: TransactionAdapter? = null

    private val transactionListViewModel: TransactionListViewModel by lazy {
        ViewModelProvider(this).get(TransactionListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)
        transactionRecyclerView = view.findViewById(R.id.transaction_recycler_view) as RecyclerView
        transactionRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    private fun updateUI(transactions: List<Transaction>) {
        adapter = TransactionAdapter(transactions)
        transactionRecyclerView.adapter = adapter
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
                transactionDateTextView.text =
                    SimpleDateFormat("d.m.YYYY. ").format(transaction.date)
                transactionTypeTextView.text = transaction.transactionType.toString()
                transactionDescriptionTextView.text = transaction.description
                //transactionAmountTextView.text = transaction.amount.toString()
            }
        }

        override fun getItemCount(): Int {
            return transactions.size
        }

    }

    companion object {
        fun newInstance(): TransactionListFragment {
            return TransactionListFragment()
        }
    }
}
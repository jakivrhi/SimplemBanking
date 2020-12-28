package com.example.simplymbanking.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplymbanking.R
import com.example.simplymbanking.models.User
import com.example.simplymbanking.viewmodels.UserListViewModel
import java.util.*

private const val TAG = "RegisteredUsersList"

class RegisteredUsersListFragment : DialogFragment() {

    interface ChosenRegisteredUser {
        fun sendChosenRegisteredUser(id: UUID)
    }

    private var chosenRegisteredUser: ChosenRegisteredUser? = null
    private lateinit var registeredUsersRecyclerView: RecyclerView
    private var adapter: RegisteredUsersAdapter? = RegisteredUsersAdapter(emptyList())

    private val userListViewModel: UserListViewModel by lazy {
        ViewModelProvider(this).get(UserListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            chosenRegisteredUser = targetFragment as ChosenRegisteredUser?
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach: ClassCastException" + e.message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registered_users_list, container, false)
        registeredUsersRecyclerView =
            view.findViewById(R.id.registered_users_recycler_view) as RecyclerView
        registeredUsersRecyclerView.layoutManager = LinearLayoutManager(context)
        registeredUsersRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userListViewModel.userListLiveData.observe(
            viewLifecycleOwner, Observer { registeredUsers ->
                registeredUsers?.let {
                    updateUI(registeredUsers)
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

    }

    private inner class RegisteredUsersHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var user: User
        val registeredUserName: TextView =
            itemView.findViewById(R.id.registered_user_name_list_item)
        val registeredUserSurname: TextView =
            itemView.findViewById(R.id.registered_user_surname_list_item)

        fun bind(user: User) {
            this.user = user
            registeredUserName.text = user.name
            registeredUserSurname.text = user.surname
        }

        init {
            itemView.setOnClickListener {
                if (itemView.isPressed) {
                    Toast.makeText(
                        context,
                        "Clicked on ${registeredUserName.text} ${registeredUserSurname.text}",
                        Toast.LENGTH_SHORT
                    ).show()
                    chosenRegisteredUser?.sendChosenRegisteredUser(user.id)
                    dismiss()
                }
            }
        }
    }

    private inner class RegisteredUsersAdapter(var registeredUsers: List<User>) :
        RecyclerView.Adapter<RegisteredUsersHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisteredUsersHolder {
            val view = layoutInflater.inflate(R.layout.registered_users_list_item, parent, false)
            return RegisteredUsersHolder(view)
        }

        override fun onBindViewHolder(holder: RegisteredUsersHolder, position: Int) {
            val user = registeredUsers[position]

            holder.bind(user)
        }

        override fun getItemCount(): Int {
            return registeredUsers.size
        }

    }

    private fun updateUI(registeredUsers: List<User>) {
        adapter = RegisteredUsersAdapter(registeredUsers)
        registeredUsersRecyclerView.adapter = adapter
    }

}
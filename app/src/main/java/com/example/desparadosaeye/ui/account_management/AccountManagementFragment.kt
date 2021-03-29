package com.example.desparadosaeye.ui.account_management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.desparadosaeye.R

class AccountManagementFragment : Fragment() {

    private lateinit var accountManagementViewModel: AccountManagementViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        accountManagementViewModel =
                ViewModelProvider(this).get(AccountManagementViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_account_management, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        accountManagementViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
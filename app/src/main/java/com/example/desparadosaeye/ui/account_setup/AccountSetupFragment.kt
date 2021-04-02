package com.example.desparadosaeye.ui.account_setup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desparadosaeye.R

class AccountSetupFragment : Fragment() {

    companion object {
        fun newInstance() = AccountSetupFragment()
    }

    private lateinit var viewModel: AccountSetupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_setup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountSetupViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
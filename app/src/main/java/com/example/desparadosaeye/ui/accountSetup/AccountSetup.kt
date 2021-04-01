package com.example.desparadosaeye.ui.accountSetup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desparadosaeye.R

class AccountSetup : Fragment() {

    companion object {
        fun newInstance() = AccountSetup()
    }

    private lateinit var viewModel: AccountSetupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_setup_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountSetupViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
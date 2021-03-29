package com.example.desparadosaeye.ui.account_management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountManagementViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Account Management Fragment"
    }
    val text: LiveData<String> = _text
}
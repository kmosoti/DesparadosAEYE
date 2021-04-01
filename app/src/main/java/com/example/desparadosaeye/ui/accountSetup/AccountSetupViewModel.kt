package com.example.desparadosaeye.ui.accountSetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountSetupViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the Account Setup Fragment"
    }
    val text: LiveData<String> = _text
}
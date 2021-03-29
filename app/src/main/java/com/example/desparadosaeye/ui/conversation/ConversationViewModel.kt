package com.example.desparadosaeye.ui.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConversationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Conversation Fragment"
    }
    val text: LiveData<String> = _text
}
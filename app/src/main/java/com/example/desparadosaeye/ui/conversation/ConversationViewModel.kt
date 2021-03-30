package com.example.desparadosaeye.ui.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desparadosaeye.data.ApplicationMode
import com.example.desparadosaeye.data.ApplicationModel
import com.example.desparadosaeye.data.Statement

class ConversationViewModel : ViewModel() {

    val applicationModel = ApplicationModel()

    init {

    }

    fun appendStatement(statement: Statement) {

    }

    fun removeStatement(statement: Statement) {

    }

    fun setMode(applicationMode: ApplicationMode) {

    }

    fun removeStatementAt(index: Int) {
        TODO("Not yet implemented")
    }

    fun addStatementAtEnd(statement: Statement) {
        TODO("Not yet implemented")
    }
}
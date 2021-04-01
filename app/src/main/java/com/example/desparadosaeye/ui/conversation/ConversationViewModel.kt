package com.example.desparadosaeye.ui.conversation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desparadosaeye.data.ApplicationMode
import com.example.desparadosaeye.data.ApplicationModel
import com.example.desparadosaeye.data.Statement

class ConversationViewModel : ViewModel() {

    val applicationModel = ApplicationModel()

    fun setMode(applicationMode: ApplicationMode) {
        when(applicationMode) {
            ApplicationMode.LoggedOut -> {
                // navigate to login page
            }
            ApplicationMode.TrainingMode -> {
                // present message that training is enabled so the AI is currently disabled
            }
            else -> {
                // do nothing
            }
        }
    }

    fun removeStatementAt(index: Int) {
        // remove from recyclerView

    }

    fun addStatementAtEnd(statement: Statement) {
        // insert at end of recyclerView
        // scroll down to position of inserted item
    }
}
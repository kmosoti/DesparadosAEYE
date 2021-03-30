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
        // insert at end of recyclerView
        // scroll down to position of inserted item
    }

    fun removeStatement(statement: Statement) {
        // remove frmo recyclerView
    }

    fun setMode(applicationMode: ApplicationMode) {
        when(applicationMode) {
            ApplicationMode.LoggedOut -> {

            }
            ApplicationMode.TextInterfaceOnly -> {

            }
            ApplicationMode.TextAndVoiceInterface -> {

            }
            ApplicationMode.VoiceInterfaceOnly -> {

            }
            ApplicationMode.TrainingMode -> {

            }
            else {
                throw NotImplementedError("The application mode is set to an undefined state")
            }
        }
    }

    fun removeStatementAt(index: Int) {
        TODO("Not yet implemented")
    }

    fun addStatementAtEnd(statement: Statement) {
        TODO("Not yet implemented")
    }
}
package com.example.desparadosaeye.ui.conversation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desparadosaeye.data.ApplicationMode
import com.example.desparadosaeye.data.ApplicationModel
import com.example.desparadosaeye.data.Statement

class ConversationViewModel : ViewModel() {

    val applicationModel = ApplicationModel()
    private var statementAdapter = StatementAdapter(applicationModel)
    private lateinit var _conversationFragment: ConversationFragment
    var conversationFragment: ConversationFragment
        get() = _conversationFragment
        set(value) {
            _conversationFragment = value
            conversationFragment.statementsRecyclerView.adapter = statementAdapter
        }

    init {
        applicationModel.conversationViewModel = this
    }

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
        statementAdapter.notifyItemRemoved(index)
    }

    fun addStatementAtEnd(statement: Statement) {
        val index = applicationModel.statements.size - 1
        // insert at end of recyclerView
        statementAdapter.notifyItemInserted(index)
        // scroll down to position of inserted item
        conversationFragment.statementsRecyclerView.scrollToPosition(index)
    }
}
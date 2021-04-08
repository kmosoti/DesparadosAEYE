package com.example.desparadosaeye.data

import android.annotation.SuppressLint
import com.example.desparadosaeye.ui.conversation.ConversationViewModel
import com.example.desparadosaeye.utils.ContentFilter
import com.example.desparadosaeye.ai.ConversationMLModel
import java.text.SimpleDateFormat
import java.util.*

const val MAX_STATEMENTS_LENGTH = 8192

class ApplicationModel {

    val statements: MutableList<Statement>

    val contentFilter = ContentFilter()
    var conversationViewModel: ConversationViewModel? = null

    private var _applicationMode = ApplicationMode.LoggedOut
    var applicationMode: ApplicationMode
        get() = _applicationMode
        set(value) {
            /* Android will take care of system resources for us.
             * Don't worry about explicitly calling open and close
            if (_applicationMode == ApplicationMode.LoggedOut &&
                value != ApplicationMode.LoggedOut) {
                conversationMLModel = ConversationMLModel()
            }
            else if (_applicationMode != ApplicationMode.LoggedOut &&
                value == ApplicationMode.LoggedOut) {
                conversationMLModel.close()
            } */

            _applicationMode = value
            conversationViewModel?.setMode(applicationMode)
        }

    init {
        statements = mutableListOf(
            Statement(
                StatementOrigin.USER,
                "hello, how are you",
                "2021-03-30T19:20:30-05:00"
            ),
            Statement(
                StatementOrigin.AI,
                "I am well. And you?",
                "2021-03-30T19:20:30-07:00"
            ),
            Statement(
                StatementOrigin.USER,
                "very good ... especially now that you're working",
                "2021-03-30T19:20:30-11:00"
            ),
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun addStatement(origin: StatementOrigin, content: String) {

        // truncate _statements from begining if too long
        while (statements.size >= MAX_STATEMENTS_LENGTH) {
            statements.removeFirst()
        }

        // filter content
        val filteredContent = contentFilter.filter(content)

        // get timestamp
        val dateCreated = SimpleDateFormat("yyyy-MM-dd").format(Date())

        // create statement
        val statement = Statement(origin, filteredContent, dateCreated)

        // add statement to model
        statements.add(statement)

        // maybe update user interface
        conversationViewModel?.notifyStatementAdded(statements.size - 1)
    }

    fun removeStatement(statement: Statement) {
        val index = statements.lastIndexOf(statement)
        statements.removeAt(index)

        // maybe update user interface
        conversationViewModel?.notifyStatementRemoved(index)
    }
}
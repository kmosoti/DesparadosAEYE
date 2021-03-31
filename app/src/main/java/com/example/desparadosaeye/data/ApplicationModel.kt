package com.example.desparadosaeye.data

import com.example.desparadosaeye.ui.conversation.ConversationViewModel
import com.example.desparadosaeye.utils.ContentFilter
import com.example.desparadosaeye.utils.ConversationMLModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

const val MAX_STATEMENTS_LENGTH = 8192

class ApplicationModel {

    val statements: MutableList<Statement>

    val contentFilter = ContentFilter()
    val conversationMLModel = ConversationMLModel()
    var conversationViewModel: ConversationViewModel? = null

    private var _applicationMode = ApplicationMode.LoggedOut
    var applicationMode: ApplicationMode
        get() = _applicationMode
        set(value) {
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

    private fun addStatement(origin: StatementOrigin, content: String) {

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
        conversationViewModel?.addStatementAtEnd(statement)
    }

    fun respondToUserInput(input: String) {
        // add user's statement
        addStatement(StatementOrigin.USER, input)

        // get AI's response
        val output = conversationMLModel.respond(statements)

        // add AI's response
        addStatement(StatementOrigin.AI, output)
    }

    fun removeStatement(statement: Statement) {
        val index = statements.lastIndexOf(statement)
        statements.removeAt(index)

        // maybe update user interface
        conversationViewModel?.removeStatementAt(index)
    }
}
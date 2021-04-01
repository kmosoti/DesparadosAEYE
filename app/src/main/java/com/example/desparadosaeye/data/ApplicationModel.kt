package com.example.desparadosaeye.data

import com.example.desparadosaeye.ui.conversation.ConversationViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

const val MAX_STATEMENTS_LENGTH = 8192

class ApplicationModel {

    val statements: MutableList<Statement>
    var conversationViewModel: ConversationViewModel? = null
    val banned = arrayOf("xyz", "abc")
    // TODO("banned = load banned words as newline separated list")
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
        val filteredContent = filterContent(content)

        // get timestamp
        val dateCreated = SimpleDateFormat("yyyy-MM-dd").format(Date())

        // create statement
        val statement = Statement(origin, content, dateCreated)

        // add statement to model
        statements.add(statement)

        // maybe update user interface
        conversationViewModel?.addStatementAtEnd(statement)
    }

    fun respondToUserInput(input: String) {
        // add user's statement
        addStatement(StatementOrigin.USER, input)

        // get AI's response
        TODO("Add language model")
        //output = AI(input)

        // add AI's response
        //addStatement(StatementOrigin.AI, output)
    }

    fun removeStatement(statement: Statement) {
        val index = statements.lastIndexOf(statement)
        statements.removeAt(index)

        // maybe update user interface
        conversationViewModel?.removeStatementAt(index)
    }

    fun filterContent(input: String): String {
        // filter language content from both human or AI
        var modified = input
        banned.forEach {
            // https://stackoverflow.com/questions/33254492/how-to-censored-bad-words-offensive-words-in-android-studio/33254897
            val rx: Pattern = Pattern.compile("\\b$it\\b", Pattern.CASE_INSENSITIVE)
            modified = rx.matcher(modified)
                .replaceAll(String(CharArray(it.length))
                .replace('\u0000', '\u0000'))
        }
        return modified
    }
}
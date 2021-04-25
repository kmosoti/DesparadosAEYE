package com.example.desparadosaeye.data

import android.annotation.SuppressLint
import com.example.desparadosaeye.ui.conversation.ConversationViewModel
import com.example.desparadosaeye.utils.ContentFilter
import java.lang.Integer.max
import java.lang.Integer.min
import java.text.SimpleDateFormat
import java.util.*

const val MAX_STATEMENTS_LENGTH = 8192

class ApplicationModel {

    val statements: MutableList<Statement>
    var conversationViewModel: ConversationViewModel? = null

    init {
        statements = mutableListOf(/*
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
            ),*/
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun addStatement(origin: StatementOrigin, content: String) {

        // truncate _statements from begining if too long
        while (statements.size >= MAX_STATEMENTS_LENGTH) {
            statements.removeFirst()
        }

        // get timestamp
        val dateCreated = SimpleDateFormat("yyyy-MM-dd").format(Date())

        // create statement
        val statement = Statement(origin, content, dateCreated)

        // add statement to model
        statements.add(statement)
        conversationViewModel?.notifyStatementAdded(statements.size - 1)
    }

    fun removeStatement(statement: Statement) {
        val index = statements.lastIndexOf(statement)
        statements.removeAt(index)

        // maybe update user interface
        conversationViewModel?.notifyStatementRemoved(index)
    }

    fun getTrailingStatements(trailingLen: Int): String {
        var outString = ""
        val trueTrailingLen = min(trailingLen, statements.size)
        var statement: Statement
        var rightOfset: Int

        for (i in 0 until trueTrailingLen) {
            // first statement has no opening <s>
            // final statement has no closing </s>
            /* example
            "My friends are cool but they eat too many carbs.</s> "
            "<s>what kind of carbs do they eat? i don't know much about carbs.</s> "
            "<s>I'm not sure." */
            rightOfset = statements.size - trueTrailingLen + i
            statement = statements[rightOfset]
            outString += when {
                i == 0 -> "" + statement.content + "</s>\n"
                i == trueTrailingLen - 1 -> "<s>" + statement.content + "</s>"
                else -> "<s>" + statement.content + "</s>\n"
            }
        }

        return outString
    }
}
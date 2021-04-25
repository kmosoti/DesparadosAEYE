package com.example.desparadosaeye.data

import android.annotation.SuppressLint
import com.example.desparadosaeye.ui.conversation.ConversationViewModel
import java.lang.Integer.min
import java.text.SimpleDateFormat
import java.util.*

const val MAX_STATEMENTS_LENGTH = 8192
const val START_QUOTE = "<s>"
const val END_QUOTE = "</s>"

class ApplicationModel {

    val statements = arrayListOf<Statement>().toMutableList()
    var conversationViewModel: ConversationViewModel? = null

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
                trueTrailingLen - 1 == 0 -> statement.content
                i == trueTrailingLen - 1 -> START_QUOTE + statement.content
                i == 0 -> statement.content + " " + END_QUOTE + "\n"
                else -> START_QUOTE + statement.content + " " + END_QUOTE + "\n"
            }
        }

        return outString
    }
}
package com.example.desparadosaeye.ai

import com.example.desparadosaeye.data.Statement

class ConversationMLModel {

    private val gpt2Client = GPT2Client()

    fun respond(statements: List<Statement>): String {
        // repsonds to dialogue
        val input = _format_statements(statements)
        return gpt2Client.generate(input, 100)
    }

    fun train(statements: List<Statement>) {
        // modifies weights in place
    }

    /* Android will take care of system resources for us.
     * Don't worry about explicitly calling open and close
    fun close() {
        gpt2Client.close()
    } */

    fun _format_statements(statements: List<Statement>): String {
        // the AI makes context sensitive responses, so this formats the list of
        // all statements into a concatenated string.
        // Not all statements are essential to making an intelligent response
        // so an exponentially distributed random subset (few distant statements
        // and many recent statements) is selected for concatenation

        //val selectedStatements = List<Statement>(0)

        // this is a simple shortcut for now. work TODO later
        return statements[statements.size - 1].content
    }

    fun _load_parameters() {

    }

    fun _save_parameters() {

    }
}
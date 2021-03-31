package com.example.desparadosaeye.utils

import com.example.desparadosaeye.data.Statement

class ConversationMLModel {

    fun respond(statements: List<Statement>): String {
        // repsonds to dialogue
        val input = _format_statements(statements)
        return "sorry: I can't respond to $input"
    }

    fun train(statements: List<Statement>) {
        // modifies weights in place
    }

    fun _format_statements(statements: List<Statement>): String {
        // the AI makes context sensitive responses, so this formats the list of
        // all statements into a concatenated string.
        // Not all statements are essential to making an intelligent response
        // so an exponentially distributed random subset (few distant statements
        // and many recent statements) is selected for concatenation

        TODO("Implement this function")
        //val selectedStatements = List<Statement>(0)

        return "work todo"
    }

    fun _load_parameters() {

    }

    fun _save_parameters() {

    }
}
package com.example.desparadosaeye.data

enum class StatementOrigin {
    USER, AI
}

class Statement(origin: StatementOrigin, content: String, dateCreated: String) {

    private val _origin: StatementOrigin = origin
    private val _content: String = content
    private val _dateCreated: String = dateCreated

    val origin: StatementOrigin
        get() {
            return this._origin
        }
    val content: String
        get() {
            return this._content
        }
    val dateCreated: String
        get() {
            return this._dateCreated
        }
}
package com.example.desparadosaeye.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
class User(
    val userId: String,
    val displayName: String,
    val password: String,
    val matrix: Array<Array<Boolean>>
) {
    fun save() {
        TODO("save everything")
    }
}
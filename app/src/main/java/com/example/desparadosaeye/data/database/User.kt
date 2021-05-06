package com.example.desparadosaeye.data.database

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */


/***
 * @constructor User(email, firstName, lastName, password)
 * @param email:String of user email
 * @param firstName String of User first name
 * @param lastName String of user last name
 * @param password String (TO BE converted to Hash) of user password
 */
class User(
    private var email: String,
    private var firstName: String,
    private var lastName: String,
    private var password: String,
) {
    constructor(user: User) : this(user.email, user.firstName, user.lastName, user.password)

//    init {
//        generateMatrix()
//        generateUserID()
//    }

    fun save() {
        TODO("save everything, send to SQLLite and save Matrix ")

    }

    /**
     * Getters
     */
    fun getUserFirstName(): String = this.firstName
    fun getUserLastName(): String = this.lastName
    fun getUserEmail(): String = this.email
    fun getUserPassword(): String = this.password

    /**
     * Setters
     */
    fun setUserFirstName(name:String) {this.firstName = name}
    fun setUserLastName(name:String){this.lastName=name}
    fun setEmail(mail:String){this.email=mail}
    fun setPassword(pass:String){this.password = pass}

    /**
     * @param email: User email to check registration with
     * @return true if already registered, false if not registered
     */
    fun isRegistered(email: String):Boolean
    {
        TODO("Determine if the user is currently registered, if they are not return false")
        if(email!=null)
        {
            return true
        }
        return false
    }

    fun isLoggedIn():Boolean
    {
        TODO("Determine if the user is currently logged in")
    }

    private fun generateUserID()
    {
        TODO("Generate a random or sequential UserID, double-check with SQLLite that the ID Doesn't already exist")

    }

}



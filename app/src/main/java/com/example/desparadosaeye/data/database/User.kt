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
    val COLSIZE: Int = com.example.desparadosaeye.ai.COLSIZE
    val ROWSIZE: Int = com.example.desparadosaeye.ai.ROWSIZE

//    init {
//        generateMatrix()
//        generateUserID()
//    }
    lateinit var matrix: Array<Array<Boolean>>

    private fun generateMatrix()
    {
        for(i in matrix.indices)
        {
            for(j in matrix.indices)
            {
                setMatrix(i,j,false)
            }
        }
    }

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
    fun getUserMatrix():Array<Array<Boolean>> = this.matrix

    /**
     * Setters
     */
    fun setUserFirstName(name:String) {this.firstName = name}
    fun setUserLastName(name:String){this.lastName=name}
    fun setEmail(mail:String){this.email=mail}
    fun setPassword(pass:String){this.password = pass}

    /**
     * Function for modifying the matrix
     * @param rowIndex: Row to be modified
     * @param colIndex : Column to be modified
     * @param value: Value to be set in specified index
     */
    fun setMatrix(rowIndex: Int, colIndex: Int,value:Boolean)
    {
        matrix[rowIndex][colIndex] = value
    }

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



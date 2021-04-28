package com.example.desparadosaeye.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desparadosaeye.R
import com.example.desparadosaeye.data.database.DataBaseHelper
import com.example.desparadosaeye.data.database.User
import com.example.desparadosaeye.ui.MainActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val firstName = findViewById<EditText>(R.id.enterFirstName)
        val lastName = findViewById<EditText>(R.id.enterLastName)
        val email = findViewById<EditText>(R.id.enterEmail)
        val password = findViewById<EditText>(R.id.enterPassword)
        val passwordValid = findViewById<EditText>(R.id.PasswordRetype)
        val createButtonClick = findViewById<Button>(R.id.createAccount)
        val db = DataBaseHelper(this)

        createButtonClick.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            val userEmail = email.text.toString();
            val userPassword = password.text.toString();
            val userPasswordCheck = passwordValid.text.toString();
            val userNameFirst = firstName.text.toString();
            val userNameLast = lastName.text.toString();

            if(userEmail.isBlank() && userPassword.isBlank() && userPassword.isBlank()){
                Toast.makeText(this, "Empty Fields.", Toast.LENGTH_SHORT ).show()
            }
            else if(userPassword != userPasswordCheck){
                Toast.makeText(this, "Passwords don't match!!", Toast.LENGTH_SHORT ).show()
            }
            else{
                val newUser = User(userEmail, userNameFirst, userNameLast, userPassword)
                db.addUser(newUser)
                Toast.makeText(this, "New User Created", Toast.LENGTH_SHORT ).show()
                startActivity(intent)
            }
        }
    }
}
package com.example.desparadosaeye.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

import com.example.desparadosaeye.R
import com.example.desparadosaeye.data.database.DataBaseHelper
import com.example.desparadosaeye.ui.MainActivity
import com.example.desparadosaeye.data.database.User
import com.example.desparadosaeye.ui.login.ForgotPassword

class LoginActivity : AppCompatActivity() {
    //private val username = findViewById<EditText>(R.id.TextEmailAddress)
   // private val password = findViewById<EditText>(R.id.TextPassword)
    //private val signInPress = findViewById<Button>(R.id.buttonSignIn);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // make animated eye
        Glide.with(this).load(R.drawable.anim).into(
            findViewById(R.id.loginLogoImageView))

        //Define Variables to be used for Account Processing
        val useremail = findViewById<EditText>(R.id.TextEmailAddress)
        val password = findViewById<EditText>(R.id.TextPassword)
        val signInButton = findViewById<Button>(R.id.buttonSignIn)
        val signUpButton = findViewById<TextView>(R.id.TextViewCreateAccount)
        val forgot_userClick = findViewById<TextView>(R.id.forgot_password)
        val DB = DataBaseHelper(this)

        signInButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            val userEmail = useremail.text.toString()
            val checkPassword = password.text.toString()
            val loginAttempt = User(userEmail,"","",checkPassword)
            val isValidEmail = DB.check_valid_user(loginAttempt)
            //val isValidPassword = DB.check_password(loginAttempt, checkPassword)
            Log.d("LoginMarker", "Valid Login Check:$isValidEmail")
            //Logic Statements to check if user credentials are valid and not empty string
            if(isValidEmail) {
                startActivity(intent)
            }
            else if(userEmail.isBlank() or checkPassword.isEmpty()){
                Toast.makeText(this, "Empty Credentials. Try Again.", Toast.LENGTH_SHORT ).show()
            }
            else {
                Toast.makeText(this, "User Account not Found", Toast.LENGTH_SHORT ).show()
            }
        }

        signUpButton.setOnClickListener{
            val intentValid = Intent(this, MainActivity::class.java)
            val intentNewUser = Intent(this, SignUpActivity::class.java)
            val userEmail = useremail.text.toString()
            val checkPassword = password.text.toString()
            val loginAttempt = User(userEmail,"","",checkPassword)
            val isValid = DB.check_valid_user(loginAttempt)
            Log.d("LoginMarker", "Valid Login Check:$isValid")
            if(isValid) {
                if(DB.check_password(loginAttempt, checkPassword)){
                    startActivity(intentValid)
                }
                else{
                    Toast.makeText(this, "User Email Found: Incorrect Password", Toast.LENGTH_SHORT ).show()
                }
            }
            else if(userEmail.isBlank() or checkPassword.isEmpty()){
                Toast.makeText(this, "Empty Credentials. Try Again.", Toast.LENGTH_SHORT ).show()
            }
            else {
                Toast.makeText(this, "Set Up New User", Toast.LENGTH_SHORT ).show()
                startActivity(intentNewUser)
            }
        }
        forgot_userClick.setOnClickListener{
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


}
package com.example.desparadosaeye.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.desparadosaeye.R
import com.example.desparadosaeye.data.database.DataBaseHelper
import com.example.desparadosaeye.data.database.User
import com.example.desparadosaeye.ui.MainActivity

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val emailVerify = findViewById<EditText>(R.id.verifyEmailText)
        val verifyButton = findViewById<Button>(R.id.verifyButton)
        val db = DataBaseHelper(this)

        verifyButton.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            val emailString = emailVerify.text.toString()
            val passwordField = "--"
            val verifyAttempt = User(emailString,"","",passwordField)
            val isValid = db.check_valid_user(verifyAttempt)
            Log.d("LoginMarker", "Verified Email Check:${isValid}")
            if(isValid){
                Toast.makeText(this, "User Account Found", Toast.LENGTH_SHORT ).show()
                intent.putExtra("EMAIL_EXTRA",emailString)
                startActivity(intent)
            }
            else if(emailString.isBlank()){
                Toast.makeText(this, "Empty Field. Try Again.", Toast.LENGTH_SHORT ).show()
            }
            else{
                Toast.makeText(this, "User Account Not Found. You must create an Account", Toast.LENGTH_SHORT ).show()
            }
        }
    }
}

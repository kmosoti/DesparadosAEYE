package com.example.desparadosaeye.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desparadosaeye.R
import com.example.desparadosaeye.data.database.DataBaseHelper
import com.example.desparadosaeye.ui.MainActivity

class ChangePassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        val userEmail = intent.getStringExtra("EMAIL_EXTRA")
        val password = findViewById<EditText>(R.id.enterPasswordReset)
        val passwordRetype = findViewById<EditText>(R.id.retypePasswordReset)
        val resetButton = findViewById<Button>(R.id.resetButton)
        val db = DataBaseHelper(this)

        resetButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val stringPassword = password.text.toString()
            val stringPasswordRetype = passwordRetype.text.toString()
            if(stringPassword != stringPasswordRetype){
                Toast.makeText(this, "Passwords don't match. Try again.", Toast.LENGTH_SHORT ).show()
            }
            else if(db.change_password(userEmail, stringPassword)){
                Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT ).show()
                startActivity(intent)
            }
        }



    }
}
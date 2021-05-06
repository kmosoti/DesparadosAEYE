package com.example.desparadosaeye.ui.account_management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.desparadosaeye.R
import com.example.desparadosaeye.data.database.DataBaseHelper
import com.example.desparadosaeye.data.database.User

class AccountManagementFragment : Fragment() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var oldEmailEditText: EditText
    private lateinit var newEmailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var db: DataBaseHelper

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account_management, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DataBaseHelper(context)

        view.findViewById<Button>(R.id.buttonSaveUserData).setOnClickListener { saveChanges()  }
        view.findViewById<Button>(R.id.buttonDeleteAccount).setOnClickListener { deleteAccount() }

        firstNameEditText = view.findViewById(R.id.editTextTextPersonFirstName)
        lastNameEditText = view.findViewById(R.id.editTextTextPersonLastName)
        oldEmailEditText = view.findViewById(R.id.editTextOldUserEmail)
        newEmailEditText = view.findViewById(R.id.editTextNewUserEmail)
        passwordEditText = view.findViewById(R.id.editTextNewPassword)
    }

    private fun saveChanges() {
        if (oldEmailEditText.text.toString().trim().isEmpty()) {
            // please supply email
            Toast.makeText(context, "Please supply your email so we can find your record", Toast.LENGTH_SHORT ).show()
            return
        }
        Log.i("changeUser", "calling change user function")
        val sucess = db.changeUser(oldEmailEditText.text.toString(),
            User(
                newEmailEditText.text.toString(),
                firstNameEditText.text.toString(),
                lastNameEditText.text.toString(),
                passwordEditText.text.toString()))
        if (sucess) {
            Toast.makeText(context, "We've updated your user settings", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText(context, "Sorry. Something went wrong and we couldn't update your account information", Toast.LENGTH_SHORT ).show()
        }
    }

    private fun deleteAccount() {

        val email = if (!oldEmailEditText.text.toString().trim().isEmpty())
            oldEmailEditText.text.toString().trim() else newEmailEditText.text.toString().trim()

        if (email.isEmpty() && passwordEditText.text.toString().trim().isEmpty()) {
            // please supply email and password
            Toast.makeText(context, "Please supply your email and password so we know who to delete", Toast.LENGTH_SHORT ).show()
            return
        }
        if (email.isEmpty()) {
            // please supply email
            Toast.makeText(context, "Please supply your email so we know who to delete", Toast.LENGTH_SHORT ).show()
            return
        }
        if (passwordEditText.text.toString().trim().isEmpty()) {
            // please supply password
            Toast.makeText(context, "Please supply your pasword so we can make sure you're really $email", Toast.LENGTH_SHORT ).show()
            return
        }

        if (!db.check_valid_user(User(
            email,
            firstNameEditText.text.toString(),
            lastNameEditText.text.toString(),
            passwordEditText.text.toString()))) {
            // password and email do not match
            Toast.makeText(context, "Your password and email do not match", Toast.LENGTH_SHORT ).show()
            return
        }

        db.deleteUser(email)
        activity?.finish()
    }
}
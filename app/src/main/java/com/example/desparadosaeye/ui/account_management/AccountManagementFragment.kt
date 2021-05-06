package com.example.desparadosaeye.ui.account_management

import android.os.Bundle
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

    private lateinit var accountManagementViewModel: AccountManagementViewModel
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        accountManagementViewModel =
                ViewModelProvider(this).get(AccountManagementViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_account_management, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonSaveUserData).setOnClickListener { saveChanges()  }
        view.findViewById<Button>(R.id.buttonDeleteAccount).setOnClickListener { deleteAccount() }

        firstNameEditText = view.findViewById(R.id.editTextTextPersonFirstName)
        lastNameEditText = view.findViewById(R.id.editTextTextPersonLastName)
        emailEditText = view.findViewById(R.id.editTextUserEmail)
        passwordEditText = view.findViewById(R.id.editTextNewPassword)
    }

    private fun saveChanges() {
        val db = DataBaseHelper(context)
        if (emailEditText.text.toString().trim().isEmpty()) {
            // please supply email
            Toast.makeText(context, "Please supply your email", Toast.LENGTH_SHORT ).show()
            return
        }
        if (!db.changeUser(emailEditText.text.toString(),
            User(
                emailEditText.text.toString(),
                firstNameEditText.text.toString(),
                lastNameEditText.text.toString(),
                passwordEditText.text.toString()
        ))) {
            // something went wrong
            Toast.makeText(context, "Oops. Something went wrong. Please try again", Toast.LENGTH_SHORT ).show()
            return
        }
    }

    private fun deleteAccount() {

        if (emailEditText.text.toString().trim().isEmpty() &&
            passwordEditText.text.toString().trim().isEmpty()) {
            // please supply email and password
            Toast.makeText(context, "Please supply your email and password", Toast.LENGTH_SHORT ).show()
            return
        }
        if (emailEditText.text.toString().trim().isEmpty()) {
            // please supply email
            Toast.makeText(context, "Please supply your email", Toast.LENGTH_SHORT ).show()
            return
        }
        if (passwordEditText.text.toString().trim().isEmpty()) {
            // please supply password
            Toast.makeText(context, "Please supply your pasword", Toast.LENGTH_SHORT ).show()
            return
        }

        val db = DataBaseHelper(context)
        if (!db.check_valid_user(User(
            emailEditText.text.toString(),
            firstNameEditText.text.toString(),
            lastNameEditText.text.toString(),
            passwordEditText.text.toString()))) {
            // password and email do not match
            Toast.makeText(context, "Password and email do not match", Toast.LENGTH_SHORT ).show()
            return
        }

        db.deleteUser(emailEditText.text.toString())
        activity?.finish()
    }
}
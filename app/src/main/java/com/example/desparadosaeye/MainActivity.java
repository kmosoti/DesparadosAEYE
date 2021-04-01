package com.example.desparadosaeye;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
        Button buttonSignIn;
        EditText TextEmailAddress,TextPassword;
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                buttonSignIn = findViewById(R.id.buttonSignIn);
                TextEmailAddress = findViewById(R.id.TextEmailAddress);
                TextPassword = findViewById(R.id.TextPassword);

                //Button Listeners
                buttonSignIn.setOnClickListener(new View.OnClickListener(){
                        @Override

                        public void onClick(View v){
                                loginModel newLogin;
                                try {
                                        newLogin = new loginModel(TextEmailAddress.getText().toString(), TextPassword.getText().toString());
                                        Toast.makeText(com.example.desparadosaeye.MainActivity.this, newLogin.toString(), Toast.LENGTH_SHORT).show();
                                }
                                catch(Exception e){
                                        Toast.makeText(com.example.desparadosaeye.MainActivity.this, "Error creating User", Toast.LENGTH_SHORT).show();
                                        newLogin = new loginModel("None","---");
                                }
                                DataBaseHelper dataBaseHelper = new DataBaseHelper(com.example.desparadosaeye.MainActivity.this);

                                boolean success = dataBaseHelper.addUser(newLogin);

                                Toast.makeText(com.example.desparadosaeye.MainActivity.this, "Success="+success, Toast.LENGTH_SHORT).show();
                        }

                });
        }
}
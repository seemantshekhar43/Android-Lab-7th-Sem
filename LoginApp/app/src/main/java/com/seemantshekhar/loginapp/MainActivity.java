package com.seemantshekhar.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logIN();
            }
        });
    }

    // Function to login
    private void logIN(){
        if(usernameEditText.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Enter a valid Username", Toast.LENGTH_SHORT).show();
        }else if(passwordEditText.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
        }else{
            boolean isValid = validateCredentials(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            if(isValid){
                Toast.makeText(MainActivity.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Function to validate login credentials
    private boolean validateCredentials(String username, String password){
        if(!username.equals("demo") || !password.equals("demo@123")){
            return false;
        }
        return true;
    }
}
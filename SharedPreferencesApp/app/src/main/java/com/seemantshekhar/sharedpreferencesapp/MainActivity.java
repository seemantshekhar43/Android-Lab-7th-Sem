package com.seemantshekhar.sharedpreferencesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText nameEditText;
    EditText emailEditText;
    EditText cityEditText;
    Button saveBtn;

    private static final String MY_PREFERENCES = "MyPrefs" ;
    private static final String NAME = "nameKey";
    private static final String CITY = "cityKey";
    private static final String EMAIL = "emailKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        cityEditText = findViewById(R.id.city_edit_text);
        saveBtn = findViewById(R.id.save_btn);

        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        //Retrieve if data exists
        String savedName = sharedPreferences.getString(NAME, "");
        String savedCity = sharedPreferences.getString(CITY, "");
        String savedEmail = sharedPreferences.getString(EMAIL, "");

        nameEditText.setText(savedName);
        cityEditText.setText(savedCity);
        emailEditText.setText(savedEmail);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameEditText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Enter a valid name.",Toast.LENGTH_LONG).show();
                }else if(emailEditText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Enter a valid email.",Toast.LENGTH_LONG).show();
                }else if(cityEditText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Enter a valid city.",Toast.LENGTH_LONG).show();
                }else{
                    String name  = nameEditText.getText().toString();
                    String email  = emailEditText.getText().toString();
                    String city  = cityEditText.getText().toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(NAME, name);
                    editor.putString(EMAIL, email);
                    editor.putString(CITY, city);
                    editor.apply();

                    Toast.makeText(MainActivity.this,"Details Saved Successfully!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
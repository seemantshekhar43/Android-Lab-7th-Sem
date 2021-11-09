package com.seemantshekhar.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private Button button3;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        textView = findViewById(R.id.textView);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                switch (button.getText().toString()){
                    case "Button 1":
                        textView.setText("Hello World Button 1 pressed!");
                        break;
                    case "Button 2":
                        textView.setText("Hello World Button 2 pressed!");
                        break;
                    case "Button 3":
                        textView.setText("Hello World Button 3 pressed!");
                        break;
                    default:
                }
            }
        };

        button1.setOnClickListener(clickListener);
        button2.setOnClickListener(clickListener);
        button3.setOnClickListener(clickListener);


    }
}
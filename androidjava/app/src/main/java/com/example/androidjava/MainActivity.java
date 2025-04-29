package com.example.androidjava;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*TextView text = new TextView(this);
        text.setTextSize(20);
        text.setTextColor(getResources().getColor(R.color.gold));
        text.setText(R.string.hello_android);*/
        setContentView(R.layout.activity_main);
    }
}
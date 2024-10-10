package com.seldy_proj.seldy.acitiviy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.seldy_proj.seldy.R;


public class AppVersionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_version);
        TextView textView = findViewById(R.id.TESTtxt);

        textView.setText(getIntent().getStringExtra("Date"));

    }
}
package com.example.sampletrialone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String nameValue = getIntent().getExtras().getString("name_key");

        TextView textView = (TextView) findViewById(R.id.name);
        textView.setText(nameValue);
    }

  }

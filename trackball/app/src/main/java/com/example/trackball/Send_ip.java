package com.example.trackball;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Send_ip extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        findViewById(R.id.submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("IP",((EditText)findViewById(R.id.etIP)).getText().toString());
        startActivity(i);
    }
}

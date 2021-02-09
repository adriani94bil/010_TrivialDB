package com.trivialdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Mensaje extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);

        TextView textView=(TextView) findViewById(R.id.respuestaView);
        TextView exView=(TextView) findViewById(R.id.explicacionView);
        if (getIntent()!=null && getIntent().hasExtra("dato")){
            textView.setText(getIntent().getStringExtra("dato"));
            exView.setText(getIntent().getStringExtra("explicacion"));
        }
    }
    public void onClickClose(View view){
        Intent returnIntent=new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
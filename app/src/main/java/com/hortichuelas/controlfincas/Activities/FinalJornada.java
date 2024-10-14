package com.hortichuelas.controlfincas.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.hortichuelas.controlfincas.R;

import java.util.PrimitiveIterator;

public class FinalJornada extends AppCompatActivity {
    private SharedPreferences prefs;
    private Context context = FinalJornada.this;
    private String fecha="1900-01-01";
    private String hora = "00:00:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_jornada);
        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            fecha = bundle.getString("fecha");
            hora = bundle.getString("hora");
            Toast.makeText(context, "Fecha "+fecha+" Hora "+hora, Toast.LENGTH_SHORT).show();

        }
    }

}
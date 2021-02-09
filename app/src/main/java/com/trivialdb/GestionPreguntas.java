package com.trivialdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.trivialdb.adapter.Adaptador;
import com.trivialdb.database.TrivialDataBase;
import com.trivialdb.model.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class GestionPreguntas extends AppCompatActivity {

    Adaptador miadapter;
    List<Pregunta> list= new ArrayList<Pregunta>();
    private TrivialDataBase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_preguntas);

        RecyclerView recyclerView=findViewById(R.id.listaRecyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mDB = new TrivialDataBase(GestionPreguntas.this);
        list= mDB.getListaPalabras();
        miadapter=new Adaptador(list);
        recyclerView.setAdapter(miadapter);
    }
}
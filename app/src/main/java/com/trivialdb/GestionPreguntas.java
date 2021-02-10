package com.trivialdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.trivialdb.adapter.Adaptador;
import com.trivialdb.database.TrivialDataBase;
import com.trivialdb.model.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class GestionPreguntas extends AppCompatActivity implements View.OnClickListener{

    Adaptador miadapter;
    List<Pregunta> list= new ArrayList<Pregunta>();
    private TrivialDataBase mDB;
    public EditText editTextDescripcion;
    public EditText editTextMensaje;
    public CheckBox checkBoxNew;
    public Button btnGuardar;
    public Button btnCerrarGestion;

    public RecyclerView recyclerView;

    public Pregunta preguntaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_preguntas);

        RecyclerView recyclerView=findViewById(R.id.listaRecyclerView);

        editTextDescripcion=findViewById(R.id.editTextDescripcion);
        editTextMensaje=findViewById(R.id.editTextMensaje);
        checkBoxNew=findViewById(R.id.checkBoxNew);
        btnGuardar=findViewById(R.id.btnGuardar);
        btnCerrarGestion=findViewById(R.id.btnCerrarGestion);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mDB = new TrivialDataBase(GestionPreguntas.this);

        GestionPreguntas gestionPreguntas=new GestionPreguntas();

        btnGuardar.setOnClickListener(this);
        btnCerrarGestion.setOnClickListener(this);

        list= mDB.getListaPalabras();
        miadapter=new Adaptador(mDB,this);
        recyclerView.setAdapter(miadapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGuardar:
                Log.d("trivial", "guardar");

                if(preguntaSeleccionada == null){
                    //GRABAR NUEVO
                    boolean respuesta = this.checkBoxNew.isChecked();
                    Pregunta nueva = new Pregunta(
                            this.editTextDescripcion.getText().toString(),
                            respuesta,
                            this.editTextMensaje.getText().toString()
                    );

                    ((Adaptador) recyclerView.getAdapter()).addPregunta(nueva);

                }else {
                    // MODIFICAR
                    preguntaSeleccionada.setDescripcion(editTextDescripcion.getText().toString());
                    preguntaSeleccionada.setMensaje(editTextMensaje.getText().toString());
                    preguntaSeleccionada.setCorrecto(checkBoxNew.isChecked());
                    ((Adaptador) recyclerView.getAdapter())
                            .update(preguntaSeleccionada);
                    preguntaSeleccionada = null;
                }
                break;
            case R.id.btnCerrarGestion:
                finish();
                break;

        }
    }
}
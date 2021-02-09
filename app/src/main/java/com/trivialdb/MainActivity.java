package com.trivialdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.trivialdb.database.TrivialDataBase;
import com.trivialdb.model.Pregunta;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnVerdadero;
    private Button btnFalso;
    private Button btnBack;
    private Button btnForward;
    private TextView textViewPregunta;
    private TextView avisoView;
    private int idPreguntaActual=0;
    private int sumatorio=0;
    private List<Pregunta> listaPregunta;
    private TrivialDataBase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnFalso = findViewById(R.id.btn_falso);
        this.btnVerdadero = findViewById(R.id.btn_verdadero);
        this.btnBack=findViewById(R.id.btnBack);
        this.btnBack.setEnabled(false);
        this.btnForward=findViewById(R.id.btnForward);
        this.textViewPregunta = findViewById(R.id.textViewPregunta);
        this.avisoView=findViewById(R.id.avisoView);

        mDB = new TrivialDataBase(MainActivity.this);

        listaPregunta = mDB.getListaPalabras();
        cargarListaPreguntas();
    }
    private void cargarListaPreguntas(){
        this.textViewPregunta.setText(listaPregunta.get(idPreguntaActual).getDescripcion());
    }
    private void inicializar(){

    }
    @Override
    public void onClick(View v){
        Log.i("trivial","id boton es "+v.getId());
        boolean respuesta=false;
        switch(v.getId()){
            case R.id.btn_falso:
                respuesta=false;
                break;
            case R.id.btn_verdadero:
                respuesta=true;
        }
        String res;
        String expl="";
        if(listaPregunta.get(idPreguntaActual).isCorrecto==respuesta){
            Log.i("trivial","acertó");
            res="Has acertado";
            sumatorio++;
        }else{
            Log.i("trivial","fallo");
            res="Fallastes";
            expl=listaPregunta.get(idPreguntaActual).getMensaje();
        }
        this.avisoView.setText("ACIERTOS  "+sumatorio);
        Intent intent=new Intent (this, Mensaje.class);
        intent.putExtra("dato",res);
        intent.putExtra("explicacion",expl);
        startActivityForResult(intent,1);
    }
    protected void onActivityResult(int requesCode, int resultCode, Intent data){
        super.onActivityResult(requesCode, resultCode, data);
        Log.d("en main","resultado "+resultCode);
        if (resultCode==RESULT_OK){
            int numero=data.getIntExtra("resultado",0);
            if(idPreguntaActual==this.listaPregunta.size()-1){
                this.idPreguntaActual=0;
                this.sumatorio=0;
                this.btnForward.setEnabled(true);
            }else{
                this.idPreguntaActual++;
                this.btnBack.setEnabled(true);

            }
            cargarListaPreguntas();
            Log.i("main","resultado es"+ numero);
        }
    }
    public void onClickBack(View v){
        if (idPreguntaActual==0){
            this.btnBack.setEnabled(false);
        }else{
            idPreguntaActual--;
            this.btnForward.setEnabled(true);
            cargarListaPreguntas();
        }
    }
    public void onClickForward(View V){
        if (idPreguntaActual==listaPregunta.size()-1){
            this.btnForward.setEnabled(false);

        }else{
            idPreguntaActual++;
            this.btnBack.setEnabled(true);
            cargarListaPreguntas();
        }

    }
    public void onClickMantenimiento(View v){
        Intent intent=new Intent (this, GestionPreguntas.class);
        startActivityForResult(intent,1);
    }
}
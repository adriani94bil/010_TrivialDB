package com.trivialdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
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

        registerForContextMenu(this.textViewPregunta);
        mDB = new TrivialDataBase(MainActivity.this);

        listaPregunta = mDB.getListaPalabras();
        cargarListaPreguntas();
    }
    private void cargarListaPreguntas(){
        listaPregunta = mDB.getListaPalabras();
        this.textViewPregunta.setText(listaPregunta.get(idPreguntaActual).getDescripcion());
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
                cargarListaPreguntas();
            }else{
                this.idPreguntaActual++;
                this.btnBack.setEnabled(true);
                cargarListaPreguntas();

            }
            if (requesCode==2){
                this.cargarListaPreguntas();

            }
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


    //MENU (Enlace + modoNight)


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Enlace a mantenimiento
        if (item.getItemId()==R.id.opcionMto){
            Intent intent=new Intent (this, GestionPreguntas.class);
            startActivityForResult(intent,1);
            return true;

            //Modo Noche
        }else if(item.getItemId()==R.id.nightMode){
            if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_NO){

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            return true;
        }else{

            return super.onContextItemSelected(item);
        }
    }

    //Menu Contextual

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId()==R.id.textViewPregunta){
            getMenuInflater().inflate(R.menu.menu_juego,menu);

        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.opcionReiniciar){
            cargarListaPreguntas();
            return true;
        }else{
            return super.onContextItemSelected(item);
        }
    }
}
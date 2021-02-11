package com.trivialdb.adapter;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.trivialdb.GestionPreguntas;
import com.trivialdb.R;
import com.trivialdb.database.TrivialDataBase;
import com.trivialdb.model.Pregunta;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MiViewHolder> {

    private List<Pregunta> listaPregunta;
    private TrivialDataBase mDB;
    private GestionPreguntas mtoActivity;

    private int posicionSel = 0;


    public Adaptador( TrivialDataBase db, GestionPreguntas mtoActivity) {

        this.mDB=db;
        this.mtoActivity=mtoActivity;
        listaPregunta=mDB.getListaPalabras();
    }

    @NonNull
    @Override
    public MiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        // la plantilla de cada item va a ser item_pregunta
        View view= inflater.inflate(R.layout.item_pregunta, parent, false);
        Adaptador.MiViewHolder miViewHolder=new Adaptador.MiViewHolder(view);
        return miViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MiViewHolder holder, int position) {
        final Pregunta pregunta=listaPregunta.get(position);
        holder.preguntaView.setText(pregunta.getDescripcion());
        holder.mensajeView.setText(pregunta.getMensaje());
        holder.checkBox.setChecked(pregunta.isCorrecto());
        holder.botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(mtoActivity);

                builder.setTitle("Vas a borrar el registro")
                        .setMessage("Estas seguro?")
                        .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                borrar(position);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("Cancelar","Se ha cancelado la accion de borrar");
                            }
                        });

                builder.create().show();

            }
        });
        //Asigno a cada linea un onclick para seleccionarlo
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pregunta preguntaSel=listaPregunta.get(position);
                mtoActivity.editTextDescripcion.setText(preguntaSel.getDescripcion());
                mtoActivity.checkBoxNew.setChecked(preguntaSel.isCorrecto());
                mtoActivity.editTextMensaje.setText(preguntaSel.getMensaje());
                mtoActivity.preguntaSeleccionada=preguntaSel;
                posicionSel=position;


            }
        });
    }
    public void borrar(int position){
        // Lo quito de la lista y lo borro
        mDB.borrarRegistro(listaPregunta.get(position).getId());
        listaPregunta.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount());
    }

    @Override
    public int getItemCount() {
        if (listaPregunta==null){
            return 0;
        }else{
            return listaPregunta.size();
        }
    }
    public void addPregunta(Pregunta pregunta){
        long id=mDB.crearRegistro(pregunta.getDescripcion(),pregunta.isCorrecto(),pregunta.getMensaje());
        pregunta.setId((int)id);
        this.listaPregunta.add(pregunta);
        notifyItemInserted(listaPregunta.size());
    }
    public void update(Pregunta pregunta){
        mDB.modificarRegistro(pregunta);
        notifyItemChanged(posicionSel);
        posicionSel = 0;
    }
    public class MiViewHolder extends RecyclerView.ViewHolder{

        //manda los datos al item_pregunta
        public TextView preguntaView;
        public TextView mensajeView;
        public CheckBox checkBox;
        public Button botonBorrar;


        public MiViewHolder(@NonNull View itemView) {
            super(itemView);
            preguntaView=itemView.findViewById(R.id.preguntaView);
            mensajeView=itemView.findViewById(R.id.mensajeView);
            checkBox=itemView.findViewById(R.id.checkBox);
            botonBorrar=itemView.findViewById(R.id.btnBorrar);
        }
        // Pasa los datos a la vista
    }
}

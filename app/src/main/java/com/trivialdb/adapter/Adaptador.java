package com.trivialdb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trivialdb.R;
import com.trivialdb.database.TrivialDataBase;
import com.trivialdb.model.Pregunta;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MiViewHolder> {

    private List<Pregunta> listaPregunta;
    private TrivialDataBase mDB;

    public Adaptador(List<Pregunta> listaPregunta) {
        this.listaPregunta=listaPregunta;
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
                // Lo quito de la lista y lo borro
                listaPregunta.remove(position);
                mDB.borrarRegistro(position);
                notifyItemRangeChanged(position,getItemCount());
            }
        });
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
        this.listaPregunta.add(pregunta);
        notifyItemInserted(listaPregunta.size());
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

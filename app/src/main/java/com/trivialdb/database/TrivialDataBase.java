package com.trivialdb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.trivialdb.model.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class TrivialDataBase extends SQLiteOpenHelper {

    //Atributos
    private static  final String DATABASE_NAME="trivial.db";
    private static  final int DATABASE_VERSION=1;
    private static  final String PREGUNTAS_TABLE_CREATE="CREATE TABLE preguntas (_id INTEGER PRIMARY KEY , descripcion TEXT, isCorrecto INTEGER, mensaje TEXT)";


    public TrivialDataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PREGUNTAS_TABLE_CREATE);
        db.execSQL("INSERT INTO PREGUNTAS (descripcion, isCorrecto, mensaje)"+ "VALUES ('China es una democracia',0,  'En serio?????')");
        db.execSQL("INSERT INTO PREGUNTAS (descripcion, isCorrecto, mensaje)"+ "VALUES ('Andorra es un paraiso fiscal',0,  'No, pertenece al FMI')");
        db.execSQL("INSERT INTO PREGUNTAS (descripcion, isCorrecto, mensaje)"+ "VALUES ('El vino tiene agua',1,  'Si, tiene un alto contenido en agua')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS preguntas");
        onCreate(db);
    }
    //listado de preguntas
    public List<Pregunta> getListaPalabras(){
        List<Pregunta> lista=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        String query="SELECT _id, descripcion, isCorrecto, mensaje FROM preguntas ORDER BY descripcion ASC";
        Cursor cursor= db.rawQuery(query, null);
        while(cursor.moveToNext()){
            Pregunta pregunta=new Pregunta(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getFloat(2)==0?false:true,
                    cursor.getString(3));
            lista.add(pregunta);
        }
        return lista;
    }
    public long crearRegistro (String pregunta, boolean isCorrecto, String mensaje){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("descripcion",pregunta);
        if (isCorrecto==true){
            values.put("isCorrecto",1);
        }else{
            values.put("isCorrecto",0);
        }
        values.put("mensaje",mensaje);
        return  db.insert("PREGUNTAS", null, values);
    }
    public Pregunta buscarPorId(long id) {
        Pregunta returnVal=null;
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT _id,palabra,definicion FROM PALABRAS WHERE _id = ?", new  String[]{String .valueOf(id)});
        if (cursor.getCount()==1){
            cursor.moveToFirst();
            if (cursor.getInt(2)==0){

                returnVal=new Pregunta(
                        cursor.getInt(0),
                        cursor.getString(1),
                        false,
                        cursor.getString(3)
                );
            }else{
                returnVal=new Pregunta(
                        cursor.getInt(0),
                        cursor.getString(1),
                        true,
                        cursor.getString(3)
                );
            }
        }
        return returnVal;
    }
    public  int borrarRegistro(int id){
        SQLiteDatabase db=getWritableDatabase();
        return db.delete("PREGUNTAS","_id=?", new String[]{String.valueOf(id)});

    }
    public int modificarRegistro(Pregunta pregunta) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", pregunta.getId());
        values.put("descripcion", pregunta.getDescripcion());
        values.put("isCorrecto", pregunta.isCorrecto()? 1: 0);
        values.put("mensaje", pregunta.getMensaje());
        return db.update("preguntas",
                values,
                "_id=?",
                new String[]{String.valueOf(pregunta.getId())});
    }
}

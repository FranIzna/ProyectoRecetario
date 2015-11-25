package com.example.fran.recetario1.gestores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fran.recetario1.basedatos.Ayudante;
import com.example.fran.recetario1.basedatos.Contrato;
import com.example.fran.recetario1.general.Categoria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chrno on 18/11/2015.
 */
public class GestorCategoria{
    private Ayudante abd;
    private SQLiteDatabase bd;

        public GestorCategoria(Context c){
            abd = new Ayudante(c);
            bd = abd.getWritableDatabase();
        }
        public void open() {
            bd = abd.getWritableDatabase();
//            List<Categoria> l=select();
//            int tam=l.size();
//            System.out.println("TAM: "+tam);
////            for(int i=0;i<tam;i++)
////                System.out.println("CATEGORIA: "+l.get(i).getNombre());
//
//            if(tam==0) {
//                System.out.println("METEMOS CATEGORIAS");
//                Categoria c = new Categoria();
//                c.setNombre("Postre");
//                insert(c);
//                /***/
//                c = new Categoria();
//                c.setNombre("Arroz");
//                insert(c);
//                /***/
//                c = new Categoria();
//                c.setNombre("Carne");
//                insert(c);
//                /***/
//                c = new Categoria();
//                c.setNombre("Pescado");
//                insert(c);
//                /***/
//                c = new Categoria();
//                c.setNombre("Pasta");
//                insert(c);
//                /***/
//                c = new Categoria();
//                c.setNombre("Verdura");
//                insert(c);
//                /***/
//                c = new Categoria();
//                c.setNombre("Sopa");
//                insert(c);
//            }
//            String addCategorias="INSERT INTO "+Contrato.TablaCategoria.TABLA+
//                    " ("+Contrato.TablaCategoria.NOMBRECATEGORIA+") VALUES ('Postre'),('Arroz'),('Carne'),('Pescado'),('Pasta'), ('Verdura'), ('Sopa')";
        }
        public void openRead() {
            bd = abd.getReadableDatabase();
        }
        public void close() {
            abd.close();
        }

        public long insert(Categoria categoria) {
            ContentValues valores = new ContentValues();
//            valores.put(Contrato.TablaCategoria._ID, categoria.getId());
            valores.put(Contrato.TablaCategoria.NOMBRECATEGORIA, categoria.getNombre());
            long id = bd.insert(Contrato.TablaCategoria.TABLA, null, valores);
            return id;
        }

        public int delete(Categoria categoria){
            return delete(categoria.getId());
        }

        public int delete(long id){
            String condicion = Contrato.TablaCategoria._ID + " = ?";
            String[] argumentos = { id + "" };
            int cuenta = bd.delete(
                    Contrato.TablaCategoria.TABLA, condicion, argumentos);
            return cuenta;
        }

        public int update(Categoria categoria){
            ContentValues valores = new ContentValues();
            valores.put(Contrato.TablaCategoria._ID, categoria.getId());
            valores.put(Contrato.TablaCategoria.NOMBRECATEGORIA, categoria.getNombre());
            String condicion = Contrato.TablaCategoria._ID + " = ?";
            String[] argumentos = { categoria.getId() + "" };
            int cuenta = bd.update(Contrato.TablaCategoria.TABLA, valores,
                    condicion, argumentos);
            return cuenta;
        }

        public Categoria getRow(Cursor c) {
            Categoria categoria = new Categoria();
            categoria.setId(c.getInt(c.getColumnIndex(Contrato.TablaCategoria._ID)));
            categoria.setNombre(c.getString(c.getColumnIndex(Contrato.TablaCategoria.NOMBRECATEGORIA)));
            return categoria;
        }

        public Categoria getRow(long id) {
            Cursor c = getCursor("_id = ?", new String[]{id+""});
            return getRow(c);
        }

        public Cursor getCursor(){
            return getCursor(null, null);
        }

        public Cursor getCursor(String condicion, String[] parametros) {
            Cursor cursor = bd.query(
                    Contrato.TablaCategoria.TABLA, null, condicion, parametros, null,
                    null, Contrato.TablaCategoria._ID+", "+Contrato.TablaCategoria.NOMBRECATEGORIA);
            return cursor;
        }

        public List<Categoria> select(String condicion, String[] parametros) {
            List<Categoria> la = new ArrayList<>();
            Cursor cursor = getCursor(condicion, parametros);
            Categoria categoria;
            while (cursor.moveToNext()) {
                categoria = getRow(cursor);
                la.add(categoria);
            }
            cursor.close();
            return la;
        }

        public List<Categoria> select() {
            return select(null,null);
        }
}

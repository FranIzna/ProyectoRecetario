package com.example.fran.recetario1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.carmen.recetario1.R;
import com.example.fran.recetario1.actividades.*;
import com.example.fran.recetario1.adaptador.*;
import com.example.fran.recetario1.basedatos.*;
import com.example.fran.recetario1.general.*;
import com.example.fran.recetario1.gestores.*;

import java.util.ArrayList;
import java.util.List;

public class Principal extends Activity {
    private Ayudante ayudante;
    private List<Recetario> lista = new ArrayList<>();
    private List<RecetaIngrediente> recetaIngrediente = new ArrayList<>();
    private Adaptador adaptador;
    private ListView lv;
    private final int ALTARECETA = 1,EDITAR=2;

    //Base de Datos
    private SQLiteDatabase bd;
    private GestorRecetario gr;
    private GestorRecetarioIngrediente gRI;
    private GestorCategoria gc;
    private GestorIngrediente gi;
    private Spinner sp2;
    int cat=0;
    //***********************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        init();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {//creamos nuestro menu contextual
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_alta, menu);
    }
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo vistainfo =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int posicion = vistainfo.position;//cogemos la posicion del elemento pulsado en la vista
        switch(item.getItemId()){//acciones que hara nuestro menu contextual
            case R.id.action_borrar:
                borra(posicion);
                actualiza();
                return true;
            case R.id.action_editar:
                Log.v("AA", "EN CONSTRUCCION");
                editar(posicion);
                actualiza();
                return true;
            default: return super.onContextItemSelected(item);
        }
    }
    public void init() {
        lv = (ListView) findViewById(R.id.listView);
        sp2=(Spinner) findViewById(R.id.spinner2);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat =parent.getSelectedItemPosition();
//                System.out.println("A: "+a);
//                cat=Integer.parseInt(a);
                actualiza();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bbdd();
    }
    public void bbdd() {
        gr = new GestorRecetario(this);
        gRI=new GestorRecetarioIngrediente(this);
//        gi=new GestorIngrediente(this);
        gc=new GestorCategoria(this);
//        bd=ayudante.getWritableDatabase();
    }
    public void generarAdaptador(){
        if(cat==0)
            lista = gr.select();
        else lista=gr.select(" idCategoria= "+cat,null);
//        recetaIngrediente=gRI.select();

        adaptador = new Adaptador(this, R.layout.item, lista);
        lv.setAdapter(adaptador);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idReceta = lista.get(position).getIdReceta();
                Intent i = new Intent(Principal.this, ActividadRecetas.class);
                Bundle b = new Bundle();
                b.putInt("ID", idReceta);

                i.putExtras(b);
                startActivity(i);
            }
        });
        registerForContextMenu(lv);
    }

    @Override
    protected void onResume() {
//        gr.open();
//        gRI.open();
//        gi.open();
//        gc.open();

        generarAdaptador();
        rellenaSpinner();
        super.onResume();
    }
    @Override
    protected void onPause() {
//        gr.close();
//        gRI.close();
//        gi.close();
//        gc.close();
        super.onPause();
    }

    //Boton que pulsando me lleva a la actividad alta
    public void añadir(View v){
        Intent i = new Intent(this,Alta.class);

        startActivityForResult(i, ALTARECETA);
    }
    public void borra(int pos){
        Recetario aux=lista.get(pos);
        List<RecetaIngrediente> l=gRI.select(" _id = " + aux.getIdReceta(), null);
        for(RecetaIngrediente ri:l)
            gRI.delete(ri.getIdRI());

        int id=aux.getIdReceta();
        gr.delete(id);
    }
    public void editar(int pos){
        int id=lista.get(pos).getIdReceta();
        Intent i= new Intent(this, Editar.class);
        Bundle b=new Bundle();
        b.putInt("ID",id);
        i.putExtras(b);
        startActivityForResult(i, EDITAR);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ALTARECETA && resultCode == Activity.RESULT_OK) {
//            Bundle b = data.getExtras();
//            Recetario r = (Recetario)b.getSerializable("aux");
//            gr.insert(r);

            actualiza();//Actualizar adaptador
        }
        if(requestCode==EDITAR && resultCode== Activity.RESULT_OK){
//            Bundle b=data.getExtras();
//            Log.v("EDITAR", "DATOS");
            actualiza();
        }
    }
    public void actualiza(){
       generarAdaptador();
    }
    public void rellenaSpinner(){
//        bd=ayudante.getWritableDatabase();

        Spinner spinner= (Spinner) findViewById(R.id.spinner2);
        String[] queryCols=new String[]{"_id", "nombreCategoria"};
        String[] adapterCols=new String[]{"nombreCategoria"};
        int[] adapterRowViews=new int[]{android.R.id.text1};
        Cursor mycursor=gc.getCursor();

        SimpleCursorAdapter sca=new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, mycursor, adapterCols, adapterRowViews,0);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sca);
    }
}

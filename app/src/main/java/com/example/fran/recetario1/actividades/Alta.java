package com.example.fran.recetario1.actividades;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carmen.recetario1.R;
import com.example.fran.recetario1.basedatos.Ayudante;
import com.example.fran.recetario1.general.*;
import com.example.fran.recetario1.gestores.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Alta extends Activity {
    private GestorIngrediente gi;
    private GestorRecetario gr;
    private GestorRecetarioIngrediente gRI;
    private Ayudante ayudante;
    private SQLiteDatabase bd;
    private List<Ingrediente> listaIngredientes=new ArrayList<>();
    private List<EditText> arrayEditText = new ArrayList<>();
    private List<EditText> arrayCantidad = new ArrayList<>();
    private ImageView iv;
    private LinearLayout lIngrediente, lCantidad;
    private String rutaFoto="fallo";
    private Spinner sp;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta);
       inicio();
        rellenaSpinner();
    }
    @Override
    protected void onResume() {
        super.onResume();
        gr.open();
//        gRI.open();
        gi.open();
    }
    @Override
    protected void onPause() {
        gr.close();
        gRI.close();
        gi.close();
        super.onPause();
    }
    public void inicio(){
        lIngrediente= (LinearLayout) findViewById(R.id.lIngrediente);
        lCantidad= (LinearLayout) findViewById(R.id.lCantidad);
        iv=(ImageView) findViewById(R.id.ivFoto);
        sp=(Spinner) findViewById(R.id.spinner);
        Toast.makeText(this,R.string.masIngredientes,Toast.LENGTH_SHORT).show();
        ayudante=new Ayudante(this);
        gr = new GestorRecetario(this);
        gi=new GestorIngrediente(this);
        gRI=new GestorRecetarioIngrediente(this);
        bd=ayudante.getWritableDatabase();
    }
    public void tostada(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    public void añadir(View v){
        listaIngredientes=gi.select();//Seleccion de los ingredientes que tenemos en la base de datos

        TextView tvNombre, tvInstrucciones;
        tvNombre = (EditText)findViewById(R.id.tvAltaNombre);
        tvInstrucciones =(EditText)findViewById(R.id.tvAltaInstrucciones);

        String nombre=tvNombre.getText().toString(),
               intrucciones=tvInstrucciones.getText().toString();
        Recetario r = new Recetario();

        if(nombre.isEmpty() || intrucciones.isEmpty())
            tostada(getString(R.string.nombreInstruccionesVacio));//************************
        else {
            r.setNombre(tvNombre.getText().toString());
            r.setInstrucciones(tvInstrucciones.getText().toString());
            r.setCategoria((int)sp.getSelectedItemId());

            if(rutaFoto.equals("fallo")) {
                rutaFoto = "" + R.mipmap.ic_launcher;
                r.setFoto(rutaFoto);
            }else
                r.setFoto(rutaFoto);//FILTRAR

            int idReceta = (int) gr.insert(r);
            GestorRecetarioIngrediente gRI = new GestorRecetarioIngrediente(this);
            RecetaIngrediente rI;
            Ingrediente aux;
            int idIngrediente;

            List<Ingrediente> arrayIngrediente;
//            int cantidad = -1;
            String cant;
            String nombreIng;
            for (int i = 0; i < arrayEditText.size(); i++) {
                nombreIng=arrayEditText.get(i).getText().toString();
                if(nombreIng.isEmpty())
                    tostada(getString(R.string.nombreIngredienteblanco));
                else{
                    String condicion = " nombreIngrediente = '" + nombreIng + "'";
                    arrayIngrediente = gi.select(condicion, null);

                if (arrayIngrediente.size() > 0) {
                    idIngrediente = arrayIngrediente.get(0).getIdIngrediente();
                    cant=arrayCantidad.get(i).getText().toString();
                    if(cant.isEmpty())
                        tostada(getString(R.string.cantidadBlanco));
                    else{
//                        cantidad = Integer.parseInt(cant);

                        rI = new RecetaIngrediente(idReceta, idIngrediente, cant);//PONER CANTIDAD
                        gRI.insert(rI);
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                } else if (arrayIngrediente.size() == 0) {
//                    System.out.println("DEBERIAS AÑADIR LOS P***** INGREDIENTES: "
//                            + arrayEditText.get(i).getText().toString());
                    aux = new Ingrediente();
                    nombreIng=arrayEditText.get(i).getText().toString();
                    if(nombreIng.isEmpty())
                        tostada(getString(R.string.nombreIngredienteblanco));
                    else{
                        aux.setNombre(nombreIng);
//                        System.out.println("INGREDIENTE QUE NO AÑADE: " + aux);
                        idIngrediente = (int) gi.insert(aux);
//                        System.out.println("-----------------------------------asd" + idIngrediente);
                         cant=arrayCantidad.get(i).getText().toString();
                        if(cant.isEmpty())
                            tostada(getString(R.string.cantidadBlanco));
                        else{
                            rI = new RecetaIngrediente(idReceta, idIngrediente, cant);//PONER CANTIDAD
//                            System.out.println("RECETA ING: "+rI.toString());
                            gRI.insert(rI);
                            setResult(Activity.RESULT_OK);
                            finish();
                            }
                        }
                    }
                }
            }
        }
    }

    public void plus(View v){
        EditText ed=new EditText(this);
        EditText ed2=new EditText(this);
//        ed2.setInputType(2);
        ed.setHint(getString(R.string.ingrediente));
        ed2.setHint(getString(R.string.cantidad));

        arrayEditText.add(ed);
        arrayCantidad.add(ed2);
        ViewGroup.LayoutParams parametros
                =new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lIngrediente.addView(ed, parametros);
        lCantidad.addView(ed2, parametros);
    }
    public void foto(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    rutaFoto=filePath;

                    File imgFile = new  File(filePath);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        iv.setImageBitmap(myBitmap);
                    }
                }
        }
    }
    public void rellenaSpinner(){
        Spinner spinner= (Spinner) findViewById(R.id.spinner);
        String[] queryCols=new String[]{"_id", "nombreCategoria"};
        String[] adapterCols=new String[]{"nombreCategoria"};
        int[] adapterRowViews=new int[]{android.R.id.text1};
        Cursor mycursor=bd.query(true,"categoria", queryCols,null,null,null,null,null,null);

        SimpleCursorAdapter sca=new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, mycursor, adapterCols, adapterRowViews,0);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sca);
    }
}

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carmen.recetario1.R;
import com.example.fran.recetario1.basedatos.Ayudante;
import com.example.fran.recetario1.general.Ingrediente;
import com.example.fran.recetario1.general.RecetaIngrediente;
import com.example.fran.recetario1.general.Recetario;
import com.example.fran.recetario1.gestores.GestorCategoria;
import com.example.fran.recetario1.gestores.GestorIngrediente;
import com.example.fran.recetario1.gestores.GestorRecetarioIngrediente;
import com.example.fran.recetario1.gestores.GestorRecetario;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chrno on 23/11/2015.
 */
public class Editar extends Activity {
    private GestorIngrediente gi;
    private GestorRecetario gr;
    private GestorRecetarioIngrediente gRI;
    private GestorCategoria gc;
    private Ayudante ayudante;
    private List<Ingrediente> listaIngredientes=new ArrayList<>();
    private List<EditText> arrayEditText=new ArrayList<>();
    private List<EditText> arrayCantidad=new ArrayList<>();
    private ImageView ivD;
    private LinearLayout lIngrediente,lCantidad;
    private String rutaFoto="fallo";
    private Intent intent;
    private Bundle bundle;
    private Spinner sp;
    private Recetario r;
    private SQLiteDatabase bd;

    int idReceta;
    /****/
    private Button b;
    private EditText nom,instr;
    private ImageView iv;
    /****/
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta);
//        b=(Button)findViewById(R.id.btAdd);
//        b.setText("EDITAR");
        inicio();
        introducePorDefecto();
        rellenaSpinner();

    }

    public void introducePorDefecto(){
        Intent i=getIntent();
        Bundle b=i.getExtras();
        idReceta=b.getInt("ID");
        r=gr.select("_ID="+idReceta,null).get(0);
        nom=(EditText) findViewById(R.id.tvAltaNombre);
        instr=(EditText) findViewById(R.id.tvAltaInstrucciones);
        ivD=(ImageView) findViewById(R.id.ivFoto);
        nom.setText(r.getNombre());
        instr.setText(r.getInstrucciones());
        File imgFile = new  File(r.getFoto());
        if(imgFile.exists()){
            Bitmap myBitmap =
                    BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivD.setImageBitmap(myBitmap);
        }
        introduceIng();
    }
    public void introduceIng(){
        EditText ed=new EditText(this);
        EditText ed2=new EditText(this);
//        ed2.setInputType(2);
        ed.setHint(getString(R.string.ingrediente));
        ed2.setHint(getString(R.string.cantidad));

        List<Ingrediente> aux=gRI.selectArrayIng(new String[]{"" + idReceta});
        String cant;
        for(Ingrediente i:aux){
            cant=""+gRI.select(" idIngrediente ="+i.getIdIngrediente(),null).get(0).getCantidad();
            ed.setText(i.getNombre());
            ed2.setText(cant);
            arrayEditText.add(ed);
            arrayCantidad.add(ed2);
        }
        ViewGroup.LayoutParams parametros
                =new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lIngrediente.addView(ed, parametros);
        lCantidad.addView(ed2, parametros);
    }
    public void inicio(){
        intent=new Intent();
        bundle=new Bundle();
        lIngrediente= (LinearLayout) findViewById(R.id.lIngrediente);
        lCantidad= (LinearLayout) findViewById(R.id.lCantidad);
        iv=(ImageView) findViewById(R.id.ivFoto);
        sp=(Spinner) findViewById(R.id.spinner);
        Toast.makeText(this, getString(R.string.masIngredientes), Toast.LENGTH_SHORT).show();

        gr = new GestorRecetario(this);
        gc = new GestorCategoria(this);
        gi=new GestorIngrediente(this);
        gRI=new GestorRecetarioIngrediente(this);
//        bd=ayudante.getWritableDatabase();

    }
    @Override
    protected void onResume() {
        super.onResume();
        gr.open();
        gRI.open();
        gi.open();
    }
    @Override
    protected void onPause() {
        gr.close();
        gRI.close();
        gi.close();
        super.onPause();
    }
    public void tostada(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    public void añadir(View v){
        listaIngredientes=gi.select();//Seleccion de los ingredientes que tenemos en la base de datos

//        tostada("FILTAR DATOS, UPDATE ETC");
//        TextView tvNombre, tvInstrucciones;
//        tvNombre = (EditText)findViewById(R.id.tvAltaNombre);
//        tvInstrucciones =(EditText)findViewById(R.id.tvAltaInstrucciones);

        String nombre=nom.getText().toString(),
                intrucciones=instr.getText().toString();
//        Recetario r = new Recetario();
//
        if(nombre.isEmpty() || intrucciones.isEmpty())
            tostada(getString(R.string.masIngredientes));//************************
        else {
//            tostada("RECETA ANTIGUA " + r);
            r.setNombre(nom.getText().toString());
            r.setInstrucciones(instr.getText().toString());
            r.setFoto(rutaFoto);//FILTRAR
            r.setCategoria((int)sp.getSelectedItemId());

            gr.update(r);

            gRI= new GestorRecetarioIngrediente(this);
            borraGI();

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
                            rI = new RecetaIngrediente(idReceta, idIngrediente, cant);//PONER CANTIDAD
                            gRI.insert(rI);
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    } else if (arrayIngrediente.size() == 0) {
                        aux = new Ingrediente();
                        nombreIng=arrayEditText.get(i).getText().toString();
                        if(nombreIng.isEmpty())
                            tostada(getString(R.string.nombreIngredienteblanco));
                        else{
                            aux.setNombre(nombreIng);
                            idIngrediente = (int) gi.insert(aux);
                             cant=arrayCantidad.get(i).getText().toString();
                            if(cant.isEmpty())
                                tostada(getString(R.string.cantidadBlanco));
                            else{
                                rI = new RecetaIngrediente(idReceta, idIngrediente, cant);//PONER CANTIDAD
                                gRI.insert(rI);

//                                tostada("UPDATE DEL RI?");
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        }
                    }
                }
            }
        }
    }
//    public void actualizaRecetaIngr(RecetaIngrediente ri){
//        gRI.update(ri);
//    }
    public void borraGI(){
        List<RecetaIngrediente> aux=gRI.select(" idReceta = "+r.getIdReceta(),null);
        for(RecetaIngrediente ri:aux){
//            tostada("ingrediente borrado: "+ri.getIdIngrediente());
            gRI.delete(ri.getIdRI());
        }
    }
    public void plus(View v){
        EditText ed=new EditText(this);
        EditText ed2=new EditText(this);
//        ed2.setInputType(2);
        ed.setHint("Ingrediente");
        ed2.setHint("Cantidad");

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

                if(requestCode == 1 && resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    rutaFoto=filePath;
                    tostada(getString(R.string.modFoto)+": "+rutaFoto);

                    File imgFile = new  File(filePath);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        iv.setImageBitmap(myBitmap);
                    }
            }
        }
    public void rellenaSpinner(){
//        bd=ayudante.getWritableDatabase();

        Spinner spinner= (Spinner) findViewById(R.id.spinner);
        String[] queryCols=new String[]{"_id", "nombreCategoria"};
        String[] adapterCols=new String[]{"nombreCategoria"};
        int[] adapterRowViews=new int[]{android.R.id.text1};
        Cursor mycursor=gc.getCursor();

        SimpleCursorAdapter sca=new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, mycursor, adapterCols, adapterRowViews,0);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sca);
    }
}
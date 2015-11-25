package com.example.fran.recetario1.actividades;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carmen.recetario1.R;
import com.example.fran.recetario1.basedatos.Ayudante;
import com.example.fran.recetario1.general.Recetario;
import com.example.fran.recetario1.gestores.GestorIngrediente;
import com.example.fran.recetario1.gestores.GestorRecetarioIngrediente;
import com.example.fran.recetario1.gestores.GestorRecetario;

import java.io.File;

public class ActividadRecetas extends Activity {
    private Recetario r;
    private String ingredientes;
    private Ayudante ayudante;
    private GestorIngrediente gi;
    private GestorRecetario gr;
    private GestorRecetarioIngrediente gri;
    private int id;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recetacompleta);

        inicio();
    }

    static class GuardarLista{
        public TextView tv1, tv2, tv3;
        public ImageView iv;
    }

    public void inicio(){
        Intent i = getIntent();
        Bundle b = i.getExtras();
        id=b.getInt("ID");
        /*****/
            gi=new GestorIngrediente(this);
            gr=new GestorRecetario(this);
            gri=new GestorRecetarioIngrediente(this);
        /*****/
            r=gr.select(" _ID="+id,null).get(0);
            ingredientes=gri.selectIngredientes(new String[] {""+id});
        /*****/
        GuardarLista gv = new GuardarLista();

        TextView tv = (TextView)findViewById(R.id.tvNombreReceta);
        gv.tv1= tv;
        tv= (TextView)findViewById(R.id.tvInstrucciones);
        gv.tv2=tv;
        tv= (TextView)findViewById(R.id.tvIngredientes);
        gv.tv3 = tv;
        ImageView iv = (ImageView)findViewById(R.id.ivReceta);
        gv.iv = iv;

        gv.tv1.setText(getString(R.string.receta)+": "+r.getNombre());
        gv.tv2.setText(getString(R.string.ingredientes)+":\n "+ingredientes);
        gv.tv3.setText(getString(R.string.instrucciones)+":\n "+r.getInstrucciones());
//        gv.iv.

        File imgFile = new File(r.getFoto());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            gv.iv.setImageBitmap(myBitmap);
        }
    }
}

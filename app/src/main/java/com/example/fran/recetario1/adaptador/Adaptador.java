package com.example.fran.recetario1.adaptador;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carmen.recetario1.R;
import com.example.fran.recetario1.general.Recetario;

import java.io.File;
import java.util.List;

/**
 * Created by Carmen on 12/11/2015.
 */
public class Adaptador extends ArrayAdapter<Recetario>{
    private List<Recetario> lista;
    private Context contexto;
    private int res;
    private LayoutInflater i;

    public Adaptador(Context contexto, int res, List<Recetario> lista){
        super(contexto, res,lista);
        this.contexto=contexto;
        this.res=res;
        this.lista=lista;
        i = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    static class GuardarLista{
        public TextView tv1;
        public ImageView iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        GuardarLista gv = new GuardarLista();

        if(convertView==null){
            convertView = i.inflate(res, null);
            TextView tv = (TextView)convertView.findViewById(R.id.tv1);
            gv.tv1= tv;
            ImageView iv=(ImageView) convertView.findViewById(R.id.imageView);
            gv.iv=iv;
            convertView.setTag(gv);
        }else gv = (GuardarLista) convertView.getTag();
        gv.tv1.setText("" + lista.get(position).getNombre());

        String ruta="";
        try {
            ruta=lista.get(position).getFoto();
        }catch (Exception e){
            ruta=""+R.mipmap.ic_launcher;
        }
//        if(ruta.isEmpty()){
//            System.out.println("SI RUTA IN EMPTY");
//            ruta=fallo;
//        }
        File imgFile = new  File(ruta);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            gv.iv.setImageBitmap(myBitmap);
        }

        return convertView;
    }






}

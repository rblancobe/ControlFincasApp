package com.hortichuelas.controlfincas.Adapters;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hortichuelas.controlfincas.Models.Empresas;
import com.hortichuelas.controlfincas.Models.Tareas;
import com.hortichuelas.controlfincas.R;
import com.hortichuelas.controlfincas.Utils.Utils;

import java.util.ArrayList;


public class AdapterEmpresa extends RecyclerView.Adapter<AdapterEmpresa.ViewHolder> {
    private Context context;
    private ArrayList<Empresas> arrayList;
    private TextView codigo, empresa, actividad;
    private AlertDialog alert;
    private SharedPreferences prefs;


    public AdapterEmpresa(Context context, ArrayList<Empresas> arrayList, TextView codigo, TextView empresa, TextView actividad, AlertDialog alert, SharedPreferences prefs) {
        this.context = context;
        this.arrayList = arrayList;
        this.codigo = codigo;
        this.empresa = empresa;
        this.actividad = actividad;
        this.alert = alert;
        this.prefs = prefs;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_lst_empresa,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.asignData(arrayList.get(holder.getAdapterPosition()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigo.setText(arrayList.get(position).getCodigo()+"");
                empresa.setText(arrayList.get(position).getEmpresa());
                actividad.setText(arrayList.get(position).getActividad()+"");
                Utils.saveUserActividad(arrayList.get(position).getCodigo()+"",arrayList.get(position).getActividad()+"",prefs);
                alert.dismiss();
            }
        });
    }



    @Override
    public int getItemCount() {        return arrayList.size();    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView codigo, empresa, actividad;
        LinearLayout layout;


        public ViewHolder(View itemView) {
            super(itemView);
            codigo = itemView.findViewById(R.id.codigo);
            empresa = itemView.findViewById(R.id.empresa);
            actividad = itemView.findViewById(R.id.actividad);
            layout = itemView.findViewById(R.id.lyt_itm);
        }

        public void asignData( Empresas emp){
            codigo.setText(emp.getCodigo()+"");
            empresa.setText(emp.getEmpresa());
            actividad.setText(emp.getActividad()+"");
        }

    }


}

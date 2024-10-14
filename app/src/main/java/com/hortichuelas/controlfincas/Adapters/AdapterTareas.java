package com.hortichuelas.controlfincas.Adapters;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hortichuelas.controlfincas.Models.Lista;
import com.hortichuelas.controlfincas.Models.Tareas;
import com.hortichuelas.controlfincas.R;

import java.util.ArrayList;


public class AdapterTareas extends RecyclerView.Adapter<AdapterTareas.ViewHolder> {
    private Context context;
    private ArrayList<Tareas> arrayList;
    private TextView id, name,tipo;
    private AlertDialog alert;


    public AdapterTareas(Context context, ArrayList<Tareas> arrayList, TextView id, TextView name, TextView tipo, AlertDialog alert) {
        this.context = context;
        this.arrayList = arrayList;
        this.id = id;
        this.name = name;
        this.tipo = tipo;
        this.alert = alert;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_lst_tarea,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.asignData(arrayList.get(holder.getAdapterPosition()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id.setText(arrayList.get(position).getId()+"");
                name.setText(arrayList.get(position).getTarea());
                tipo.setText(arrayList.get(position).getTipo());
                if(arrayList.get(position).getTipo().equals("S")){
                    tipo.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    name.setTextColor(context.getResources().getColor(R.color.colorAccent));
                }else{
                    tipo.setTextColor(context.getResources().getColor(R.color.white));
                    name.setTextColor(context.getResources().getColor(R.color.gray));
                }
                alert.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {        return arrayList.size();    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id, name, tipo;
        LinearLayout layout;


        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.item);
            tipo = itemView.findViewById(R.id.tipo);
            layout = itemView.findViewById(R.id.lyt_itm);
        }

        public void asignData( Tareas tar){
            id.setText(tar.getId()+"");
            name.setText(tar.getTarea());
            tipo.setText(tar.getTipo());
            if(tar.getTipo().equals("S")){
                tipo.setTextColor(context.getResources().getColor(R.color.colorAccent));
                name.setTextColor(context.getResources().getColor(R.color.colorAccent));
                }else{
                tipo.setTextColor(context.getResources().getColor(R.color.white));
                name.setTextColor(context.getResources().getColor(R.color.white));
                }

        }

    }


}

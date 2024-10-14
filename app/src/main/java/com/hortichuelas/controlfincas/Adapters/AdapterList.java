package com.hortichuelas.controlfincas.Adapters;



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
import com.hortichuelas.controlfincas.R;
import java.util.ArrayList;


public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {
    private Context context;
    private ArrayList<Lista> arrayList;
    private TextView id, name;
    private AlertDialog alert;


    public AdapterList(Context context, ArrayList<Lista> arrayList,TextView id, TextView name,AlertDialog alert) {
        this.context = context;
        this.arrayList = arrayList;
        this.id = id;
        this.name = name;
        this.alert = alert;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_lst,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.asignData(arrayList.get(holder.getAdapterPosition()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id.setText(arrayList.get(position).getId()+"");
                name.setText(arrayList.get(position).getName());
                alert.dismiss();
            }
        });
    }



    @Override
    public int getItemCount() {        return arrayList.size();    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id, name;
        LinearLayout layout;


        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.item);
            layout = itemView.findViewById(R.id.lyt_itm);
        }

        public void asignData( Lista lst){
            id.setText(lst.getId()+"");
            name.setText(lst.getName());
        }

    }


}

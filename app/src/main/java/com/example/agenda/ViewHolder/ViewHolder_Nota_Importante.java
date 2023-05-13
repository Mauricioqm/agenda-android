package com.example.agenda.ViewHolder;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;


public class ViewHolder_Nota_Importante extends RecyclerView.ViewHolder {


    View mView;

    private ViewHolder_Nota_Importante.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); //Se ejecuta al precionar en el Item
        void onItemLongClick(View view, int position); //Se ejecuta al mantener presionado el Item
    }

    public void setOnClickListener(ViewHolder_Nota_Importante.ClickListener clickListerer){
        mClickListener = clickListerer;
    }

    public ViewHolder_Nota_Importante(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getBindingAdapterPosition());
                return false;
            }
        });

    }

    public void SetearDatos(Context context, String id_nota, String uid_usuario, String correo_usuario,
                            String fechas_hora_registro, String titulo, String descripcion,
                            String fecha_nota, String estado){
        //Declarar vistas
        TextView Id_nota_Item, Uid_Usuario_Item, Correo_Usuario_Item, Fecha_hora_regitro_Item,
                Titulo_Item, Descripcion_Item, Fecha_Item, Estado_Item;

        ImageView Tarea_Finalizada_Item, Tarea_No_Finalizada_Item;

        //Establecer la conexion con el Item
        Id_nota_Item = mView.findViewById(R.id.Id_nota_Item_I);
        Uid_Usuario_Item = mView.findViewById(R.id.Uid_Usuario_Item_I);
        Correo_Usuario_Item = mView.findViewById(R.id.Correo_Usuario_Item_I);
        Fecha_hora_regitro_Item = mView.findViewById(R.id.Fecha_hora_regitro_Item_I);
        Titulo_Item = mView.findViewById(R.id.Titulo_Item_I);
        Descripcion_Item = mView.findViewById(R.id.Descripcion_Item_I);
        Fecha_Item = mView.findViewById(R.id.Fecha_Item_I);
        Estado_Item = mView.findViewById(R.id.Estado_Item_I);

        Tarea_Finalizada_Item = mView.findViewById(R.id.Tarea_Finalizada_Item_I);
        Tarea_No_Finalizada_Item = mView.findViewById(R.id.Tarea_No_Finalizada_Item_I);

        //Setear la info dentro del Item
        Id_nota_Item.setText(id_nota);
        Uid_Usuario_Item.setText(uid_usuario);
        Correo_Usuario_Item.setText(correo_usuario);
        Fecha_hora_regitro_Item.setText(fechas_hora_registro);
        Titulo_Item.setText(titulo);
        Descripcion_Item.setText(descripcion);
        Fecha_Item.setText(fecha_nota);
        Estado_Item.setText(estado);

        //Gestionamos el color del estado
        if(estado.equals("Finalizado")){
            Tarea_Finalizada_Item.setVisibility(View.VISIBLE);
        }else{
            Tarea_No_Finalizada_Item.setVisibility(View.VISIBLE);
        }

    }

}

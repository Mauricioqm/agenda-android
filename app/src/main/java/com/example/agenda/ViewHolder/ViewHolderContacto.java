package com.example.agenda.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agenda.R;

public class ViewHolderContacto extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolderContacto.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); //Se ejecuta al precionar en el Item
        void onItemLongClick(View view, int position); //Se ejecuta al mantener presionado el Item
    }

    public void setOnClickListener(ViewHolderContacto.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderContacto(@NonNull View itemView) {
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

    public void SetearDatosContacto(Context context, String id_contacto, String uid_contacto,
                                    String nombres, String apellidos, String correo,
                                    String telefono, String fec_nacimiento, String edad,
                                    String institucion, String direccion, String imagen){

        ImageView Imagen_C_Item;
        TextView Id_C_Item, Uid_C_Item, nombres_C_Item, apellidos_C_Item, correo_C_Item, telefono_C_Item,
                fec_nacimiento_C_Item, edad_C_Item, institucion_C_Item, direccion_C_Item;

        Imagen_C_Item = mView.findViewById(R.id.Imagen_C_Item);
        Id_C_Item = mView.findViewById(R.id.Id_C_Item);
        Uid_C_Item = mView.findViewById(R.id.Uid_C_Item);
        nombres_C_Item = mView.findViewById(R.id.nombres_C_Item);
        apellidos_C_Item = mView.findViewById(R.id.apellidos_C_Item);
        correo_C_Item = mView.findViewById(R.id.correo_C_Item);
        telefono_C_Item = mView.findViewById(R.id.telefono_C_Item);
        fec_nacimiento_C_Item = mView.findViewById(R.id.fec_nacimiento_C_Item);
        edad_C_Item = mView.findViewById(R.id.edad_C_Item);
        institucion_C_Item = mView.findViewById(R.id.institucion_C_Item);
        direccion_C_Item = mView.findViewById(R.id.direccion_C_Item);

        Id_C_Item.setText(id_contacto);
        Uid_C_Item.setText(uid_contacto);
        nombres_C_Item.setText(nombres);
        apellidos_C_Item.setText(apellidos);
        correo_C_Item.setText(correo);
        telefono_C_Item.setText(telefono);
        fec_nacimiento_C_Item.setText(fec_nacimiento);
        edad_C_Item.setText(edad);
        institucion_C_Item.setText(institucion);
        direccion_C_Item.setText(direccion);

        try {
            //Si existe dentro de la BD, se cargara su correspondiente
            Glide.with(context).load(imagen).placeholder(R.drawable.imagen_contacto).into(Imagen_C_Item);

        }catch (Exception e){
            //Si no existe dentro de la BD, se cargara una imagen predeterminada
            Glide.with(context).load(R.drawable.imagen_contacto).into(Imagen_C_Item);
        }


    }

}

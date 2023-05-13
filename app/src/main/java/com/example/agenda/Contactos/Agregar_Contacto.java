package com.example.agenda.Contactos;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.Objetos.Contacto;
import com.example.agenda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import java.util.Calendar;

public class Agregar_Contacto extends AppCompatActivity {

  TextView Uid_Usuario_C, Fecha_Nacimiento_C, Telefono_C;
  EditText Nombres_C, Apellidos_C, Correo_C, Direccion_C, Institucion_C, Edad_C;

  Button Btn_Agregar_Contacto;

  ImageView Editar_Telefono_C, Editar_fecha_C;

  FirebaseAuth firebaseAuth;
  FirebaseUser user;
  DatabaseReference BD_Usuarios;

  Dialog dialog_establecer_telefono;
  Dialog dialog;

  int dia, mes, anio;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_agregar_contacto);


    ActionBar actionBar = getSupportActionBar();
    assert actionBar != null;
    actionBar.setTitle("Agregar Contacto");
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);


    InicializarVariables();
    ObtenerUidUsuario();
    Editar_Telefono_C.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Establecer_Telefono_Contacto();
      }
    });

    Editar_fecha_C.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {Abrir_Calendario();}
    });

    Btn_Agregar_Contacto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {AgregarContacto();}
    });
  }

  private void ObtenerUidUsuario(){
    String Uid_Recuperado = getIntent().getStringExtra("Uid");
    Uid_Usuario_C.setText(Uid_Recuperado);
  }

  private void InicializarVariables() {
    Uid_Usuario_C = findViewById(R.id.Uid_Usuario_C);
    Fecha_Nacimiento_C = findViewById(R.id.Fecha_Nacimiento_C);
    Telefono_C = findViewById(R.id.Telefono_C);

    Nombres_C = findViewById(R.id.Nombres_C);
    Apellidos_C = findViewById(R.id.Apellidos_C);
    Correo_C = findViewById(R.id.Correo_C);
    Direccion_C = findViewById(R.id.Direccion_C);
    Institucion_C = findViewById(R.id.Institucion_C);
    Edad_C = findViewById(R.id.Edad_C);


    Editar_Telefono_C = findViewById(R.id.Editar_Telefono_C);
    Editar_fecha_C = findViewById(R.id.Editar_fecha_C);

    dialog_establecer_telefono = new Dialog(Agregar_Contacto.this);
    dialog = new Dialog(Agregar_Contacto.this);

    Btn_Agregar_Contacto = findViewById(R.id.Btn_Agregar_Contacto);

    firebaseAuth = FirebaseAuth.getInstance();
    user = firebaseAuth.getCurrentUser();

    BD_Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
  }

  private void Establecer_Telefono_Contacto(){
    CountryCodePicker ccp;
    EditText Establecer_Telefono;
    Button Btn_Aceptar_Telefono;


    dialog_establecer_telefono.setContentView(R.layout.cuadro_dialogo_establecer_telefono);

    ccp = dialog_establecer_telefono.findViewById(R.id.ccp);
    Establecer_Telefono = dialog_establecer_telefono.findViewById(R.id.Establecer_Telefono);
    Btn_Aceptar_Telefono = dialog_establecer_telefono.findViewById(R.id.Btn_Aceptar_Telefono);

    Btn_Aceptar_Telefono.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String codigo_pais = ccp.getSelectedCountryCodeWithPlus();
        String telefono = Establecer_Telefono.getText().toString();
        String codigo_pais_telefono = codigo_pais+telefono; //+52 99334000000

        if (!telefono.equals("")) {
          Telefono_C.setText(codigo_pais_telefono);
          dialog_establecer_telefono.dismiss();
        }else{
          //Toast.makeText(Agregar_Contacto.this, "", Toast.LENGTH_SHORT).show();
          dialog_establecer_telefono.dismiss();
        }
      }
    });

    dialog_establecer_telefono.show();
    dialog_establecer_telefono.setCanceledOnTouchOutside(true);
  }

  private void Abrir_Calendario(){
    final Calendar calendar = Calendar.getInstance();

    dia = calendar.get(Calendar.DAY_OF_MONTH);
    mes = calendar.get(Calendar.MONTH);
    anio = calendar.get(Calendar.YEAR);

    DatePickerDialog datePickerDialog = new DatePickerDialog(Agregar_Contacto.this, new DatePickerDialog.OnDateSetListener() {
      @Override
      //S = Seleccionado
      public void onDateSet(DatePicker view, int anioS, int mesS, int diaS) {

        //F = Formateado
        String diaF, mesF;

        //Obtener DIA
        if(diaS < 10){
          diaF = "0"+String.valueOf(diaS);
          //Antes: 9/11/2022 - Ahora 09/11/2022
        }else{
          diaF = String.valueOf(diaS);
          //Ejemplo: 13/08/2022
        }

        //Obtener el MES
        int Mes = mesS + 1;
        if(Mes < 10){
          mesF = "0"+String.valueOf(Mes);
          //Antes: 9/11/2022 - Ahora 09/11/2022
        }else{
          mesF = String.valueOf(Mes);
          //Ejemplo: 13/10/2022 - 13/11/2022 - 13/12/2022
        }

        //Setear fecha en Textview
        Fecha_Nacimiento_C.setText(diaF + "/" + mesF + "/" +anioS);

      }
    }
      ,anio,mes,dia);
    datePickerDialog.show();
  }

  private void AgregarContacto(){

    //Obtener lo datos
    String uid = Uid_Usuario_C.getText().toString();
    String nombres = Nombres_C.getText().toString();
    String apellidos = Apellidos_C.getText().toString();
    String correo = Correo_C.getText().toString();
    String telefono = Telefono_C.getText().toString();
    String edad = Edad_C.getText().toString();
    String fecha_nacimiento = Fecha_Nacimiento_C.getText().toString();
    String institucion = Institucion_C.getText().toString();
    String direccion = Direccion_C.getText().toString();

    //Creamos la cadena unica
    String id_contacto = BD_Usuarios.push().getKey();
    if(!uid.equals("") && !nombres.equals("")){

      Contacto contacto = new Contacto(
        id_contacto,
        uid,
        nombres,
        apellidos,
        correo,
        telefono,
        fecha_nacimiento,
        edad,
        institucion,
        direccion,
        "");


      //Establecer el nombre de la BD
      String Nombre_BD = "Contactos";
      assert id_contacto != null;

      BD_Usuarios.child(user.getUid()).child(Nombre_BD).child(id_contacto).setValue(contacto);
      Toast.makeText(this, "¡Contacto agregado existosamente!", Toast.LENGTH_SHORT).show();
      onBackPressed();

    }else{
      ValidarRegistroContacto();
      //Toast.makeText(this, "Coloca algun nombre", Toast.LENGTH_SHORT).show();
    }
  }

  private void ValidarRegistroContacto(){

    Button Btn_Validar_Registro_C;
    dialog.setContentView(R.layout.cuadro_dialogo_validar_registro_c);

    Btn_Validar_Registro_C = dialog.findViewById(R.id.Btn_Validar_Registro_C);

    Btn_Validar_Registro_C.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    dialog.show();
    dialog.setCanceledOnTouchOutside(false);

  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return super.onSupportNavigateUp();
  }
}
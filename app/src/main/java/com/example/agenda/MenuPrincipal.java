package com.example.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agenda.Perfil.Perfil_Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipal extends AppCompatActivity {

  ImageView ImagenUsuario;

  Button AgregarNotas, ListarNotas, Importantes, Contactos, AcercaDe, CerrarSesion;

  FirebaseAuth firebaseAuth;
  FirebaseUser user;

  TextView UidPrincipal, NombresPrincipal, CorreoPrincipal;
  Button EstadoCuentaPrincipal;
  ProgressBar progressBarDatos;
  ProgressDialog progressDialog;

  LinearLayoutCompat Linear_Nombres, Linear_Correo, Linear_Verificacion;

  DatabaseReference Usuarios;

  Dialog dialog_cuenta_verificada, dialog_informacion;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu_principal);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle("");

    ImagenUsuario = findViewById(R.id.ImagenUsuario);
    UidPrincipal = findViewById(R.id.UidPrincipal);
    NombresPrincipal = findViewById(R.id.NombresPrincipal);
    CorreoPrincipal = findViewById(R.id.CorreoPrincipal);
    EstadoCuentaPrincipal = findViewById(R.id.EstadoCuentaPrincipal);
    progressBarDatos = findViewById(R.id.progressBarDatos);

    dialog_cuenta_verificada = new Dialog(this);
    dialog_informacion = new Dialog(this);

    progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Espere por favor ...");
    progressDialog.setCanceledOnTouchOutside(false);

    Linear_Nombres = findViewById(R.id.Linear_Nombres);
    Linear_Correo = findViewById(R.id.Linear_Correo);
    Linear_Verificacion = findViewById(R.id.Linear_Verificacion);

    AgregarNotas = findViewById(R.id.AgregarNotas);
    ListarNotas = findViewById(R.id.ListarNotas);
    Importantes = findViewById(R.id.Importantes);
    Contactos = findViewById(R.id.Contactos);
    AcercaDe = findViewById(R.id.AcercaDe);
    Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
    CerrarSesion = findViewById(R.id.CerrarSesion);


    firebaseAuth = FirebaseAuth.getInstance();
    user = firebaseAuth.getCurrentUser();

    EstadoCuentaPrincipal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (user.isEmailVerified()){
          //Cuenta verificada
          //Toast.makeText(MenuPrincipal.this, "Cuenta ya verificada", Toast.LENGTH_SHORT).show();
          AnimacionCuentaVerificada();
        }else{
          //Cuenta no verificada
          VerificarCuentaCorreo();
        }

      }
    });

    AgregarNotas.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        //Obtener una copia de la informacion de los TextView
        String uid_usuario = UidPrincipal.getText().toString();
        String correo_usuario = CorreoPrincipal.getText().toString();

        //Pasamos a la siguiente actividad
        /*Intent intent = new Intent(MenuPrincipal.this, Agregar_Nota.class);
        intent.putExtra("Uid",uid_usuario);
        intent.putExtra("Correo",correo_usuario);
        startActivity(intent);*/

      }
    });

    ListarNotas.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //startActivity(new Intent(MenuPrincipal.this, Listar_Notas.class));
        //Toast.makeText(MenuPrincipal.this, "Listar Notas", Toast.LENGTH_SHORT).show();
      }
    });

    Importantes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //startActivity(new Intent(MenuPrincipal.this, Notas_Importantes.class));
        //Toast.makeText(MenuPrincipal.this, "Notas Archivadas", Toast.LENGTH_SHORT).show();
      }
    });

    Contactos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //String uid_Usuario =UidPrincipal.getText().toString();
        //Intent intent = new Intent(MenuPrincipal.this, Listar_Contactos.class);
        //intent.putExtra("Uid", uid_Usuario);
        //startActivity(intent);
        //Toast.makeText(MenuPrincipal.this, "Contactos", Toast.LENGTH_SHORT).show();
      }
    });

    AcercaDe.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Informacion();
      }
    });

    CerrarSesion.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SalirApliacion();
      }
    });
  }

  private void VerificarCuentaCorreo() {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Verificar cuenta")
      .setMessage("¿Estás segur@ de enviar verificacion mediante correo electronico? "
        +user.getEmail())
      .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          EnviarCorreoAVerificacion();
        }
      })
      .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          Toast.makeText(MenuPrincipal.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
        }
      }).show();

  }

  private void AnimacionCuentaVerificada(){
    Button EntendidoVerificado;

    dialog_cuenta_verificada.setContentView(R.layout.dialogo_cuenta_verificada);
    EntendidoVerificado = dialog_cuenta_verificada.findViewById(R.id.EntendidoVerificado);

    EntendidoVerificado.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog_cuenta_verificada.dismiss();
      }
    });

    dialog_cuenta_verificada.show();
    dialog_cuenta_verificada.setCanceledOnTouchOutside(false);
  }

  private void Informacion(){
    Button EntendidoInfo;

    dialog_informacion.setContentView(R.layout.cuadro_dialogo_informacion);

    EntendidoInfo = dialog_informacion.findViewById(R.id.EntendidoInfo);
    EntendidoInfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog_informacion.dismiss();
      }
    });
    dialog_informacion.show();
    dialog_informacion.setCanceledOnTouchOutside(false);
  }

  private void EnviarCorreoAVerificacion() {
    progressDialog.setMessage("Enviando correo de verificacion "+ user.getEmail());
    progressDialog.show();

    user.sendEmailVerification()
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void unused) {
          //Envio exitoso
          progressDialog.dismiss();
          Toast.makeText(MenuPrincipal.this, "Correo enviado, revise su bandeja "+user.getEmail(), Toast.LENGTH_SHORT).show();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          //Fallo al enviar
          Toast.makeText(MenuPrincipal.this, "Error al enviar: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

  }

  private void VerificarEstadoDeCuenta(){
    String Verificado = "Verificado";
    String No_Verificado = "No Verificado";
    if (user.isEmailVerified()) {
      EstadoCuentaPrincipal.setText(Verificado);
      EstadoCuentaPrincipal.setBackgroundColor(Color.rgb(46, 204, 113));
    }else{
      EstadoCuentaPrincipal.setText(No_Verificado);
      EstadoCuentaPrincipal.setBackgroundColor(Color.rgb(225, 127, 41));
    }
  }

  @Override
  protected void onStart() {
    ComprobarInicioSesion();
    super.onStart();
  }

  private void ComprobarInicioSesion(){
    if(user!= null){
      //El usuario ha iniciado sesion
      CargaDeDatos();
    }else {
      //Lo dirigira al MainActivity
      startActivity(new Intent(MenuPrincipal.this,MainActivity.class));
    }
  }

  private void CargaDeDatos(){

    VerificarEstadoDeCuenta();

    Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {

        //Si el usuario existe
        if(snapshot.exists()){
          //Se oclta el progressbar
          progressBarDatos.setVisibility(View.GONE);

          //Los TextView se muestran
          /*UidPrincipal.setVisibility(View.VISIBLE);NombresPrincipal.setVisibility(View.VISIBLE);CorreoPrincipal.setVisibility(View.VISIBLE);*/
          Linear_Nombres.setVisibility(View.VISIBLE);
          Linear_Correo.setVisibility(View.VISIBLE);
          Linear_Verificacion.setVisibility(View.VISIBLE);

          //Obtener los datos
          String uid = "" +snapshot.child("uid").getValue();
          String nombres = "" +snapshot.child("nombre").getValue();
          String correo = ""+snapshot.child("correo").getValue();
          String imagen = ""+snapshot.child("imagen_perfil").getValue();

          //Set datos en los respectios TextView
          UidPrincipal.setText(uid);
          NombresPrincipal.setText(nombres);
          CorreoPrincipal.setText(correo);

          //Habilitar botones del Menú

          AgregarNotas.setEnabled(true);
          ListarNotas.setEnabled(true);
          Importantes.setEnabled(true);
          Contactos.setEnabled(true);
          AcercaDe.setEnabled(true);
          CerrarSesion.setEnabled(true);

          ObtenerImagen(imagen);

        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  private void ObtenerImagen(String imagen) {

    try {
      Glide.with(getApplicationContext()).load(imagen).placeholder(R.drawable.imagen_usuario).into(ImagenUsuario);
    }catch (Exception e){
      Glide.with(getApplicationContext()).load(R.drawable.imagen_usuario).into(ImagenUsuario);
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_principal,menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.Perfil_usuario){
      startActivity(new Intent(MenuPrincipal.this, Perfil_Usuario.class));
    }
    return super.onOptionsItemSelected(item);
  }

  private void SalirApliacion() {
    firebaseAuth.signOut();
    startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
    Toast.makeText(this,"Cerraste sesión exitosamente", Toast.LENGTH_SHORT).show();

  }
}
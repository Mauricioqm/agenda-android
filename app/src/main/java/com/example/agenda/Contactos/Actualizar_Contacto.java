package com.example.agenda.Contactos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agenda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;
import java.util.HashMap;


public class Actualizar_Contacto extends AppCompatActivity {

  TextView Id_C_A, Uid_C_A, Fecha_Nacimiento_C_A, Telefono_C_A;
  EditText Nombres_C_A, Apellidos_C_A, Correo_C_A, Direccion_C_A, Institucion_C_A, Edad_C_A;

  Button Btn_Actualizar_C_A;

  ImageView Imagen_C_A, Actualizar_imagen_C_A, Actualizar_Telefono_C_A, Actualizar_fecha_C_A;


  String id_c,uid_usuario, nombres_c, apellidos_c, correo_c, telefono_c, fecha_nac_c, edad_c, direccion_c, institucion_c;

  int dia, mes, anio;

  Dialog dialog_actualizar_telefono;
  Dialog dialog;

  FirebaseAuth firebaseAuth;
  FirebaseUser user;

  Uri imagenUri = null;

  ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actualizar_contacto);


    ActionBar actionBar = getSupportActionBar();
    assert actionBar != null;
    actionBar.setTitle("Actualizar contacto");
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);


    InicializarVistas();
    RecuperarDatos();
    SetearDatosRecuperados();
    ObtenerImagen();

    Actualizar_fecha_C_A.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {Abrir_Calendario();}
    });

    Actualizar_Telefono_C_A.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Establecer_Telefono_Contacto();
      }
    });

    Btn_Actualizar_C_A.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {ActualizarInformacionContacto();}
    });

    Actualizar_imagen_C_A.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(Actualizar_Contacto.this,
          Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
          SeleccionarImagenGaleria();
        }
        else{
          SolicitudPermisoGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

      }
    });

    progressDialog = new ProgressDialog(Actualizar_Contacto.this);
    progressDialog.setTitle("Espere por favor");
    progressDialog.setCanceledOnTouchOutside(false);
  }


  private void InicializarVistas() {
    Id_C_A = findViewById(R.id.Id_C_A);
    Uid_C_A = findViewById(R.id.Uid_C_A);
    Fecha_Nacimiento_C_A = findViewById(R.id.Fecha_Nacimiento_C_A);
    Telefono_C_A = findViewById(R.id.Telefono_C_A);

    Nombres_C_A = findViewById(R.id.Nombres_C_A);
    Apellidos_C_A = findViewById(R.id.Apellidos_C_A);
    Correo_C_A = findViewById(R.id.Correo_C_A);
    Direccion_C_A = findViewById(R.id.Direccion_C_A);
    Institucion_C_A = findViewById(R.id.Institucion_C_A);
    Edad_C_A = findViewById(R.id.Edad_C_A);
    Imagen_C_A = findViewById(R.id.Imagen_C_A);

    Actualizar_imagen_C_A = findViewById(R.id.Actualizar_imagen_C_A);
    Actualizar_Telefono_C_A = findViewById(R.id.Actualizar_Telefono_C_A);
    Actualizar_fecha_C_A = findViewById(R.id.Actualizar_fecha_C_A);

    Btn_Actualizar_C_A = findViewById(R.id.Btn_Actualizar_C_A);

    dialog_actualizar_telefono = new Dialog(Actualizar_Contacto.this);
    dialog = new Dialog(Actualizar_Contacto.this);

    firebaseAuth = FirebaseAuth.getInstance();
    user = firebaseAuth.getCurrentUser();

  }

  private void RecuperarDatos(){
    Bundle bundle = getIntent().getExtras();
    id_c = bundle.getString("id_c");
    uid_usuario = bundle.getString("uid_usuario");
    nombres_c = bundle.getString("nombres_c");
    apellidos_c = bundle.getString("apellidos_c");
    correo_c = bundle.getString("correo_c");
    telefono_c = bundle.getString("telefono_c");
    fecha_nac_c = bundle.getString("fecha_nacimiento_c");
    edad_c = bundle.getString("edad_c");
    institucion_c = bundle.getString("institucion_c");
    direccion_c = bundle.getString("direccion_c");


  }

  private void SetearDatosRecuperados(){
    Id_C_A.setText(id_c);
    Uid_C_A.setText(uid_usuario);
    Nombres_C_A.setText(nombres_c);
    Apellidos_C_A.setText(apellidos_c);
    Correo_C_A.setText(correo_c);
    Telefono_C_A.setText(telefono_c);
    Fecha_Nacimiento_C_A.setText(fecha_nac_c);
    Edad_C_A.setText(edad_c);
    Institucion_C_A.setText(institucion_c);
    Direccion_C_A.setText(direccion_c);
  }

  private void ObtenerImagen(){
    String imagen = getIntent().getStringExtra("imagen_c");

    try {
      Glide.with(getApplicationContext()).load(imagen).placeholder(R.drawable.imagen_contacto).into(Imagen_C_A);
    }catch (Exception e){
      Toast.makeText(this, "Esperando imagen", Toast.LENGTH_SHORT).show();
    }
  }


  private void Abrir_Calendario(){
    final Calendar calendar = Calendar.getInstance();

    dia = calendar.get(Calendar.DAY_OF_MONTH);
    mes = calendar.get(Calendar.MONTH);
    anio = calendar.get(Calendar.YEAR);

    DatePickerDialog datePickerDialog = new DatePickerDialog(Actualizar_Contacto.this, new DatePickerDialog.OnDateSetListener() {
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
        Fecha_Nacimiento_C_A.setText(diaF + "/" + mesF + "/" +anioS);

      }
    }
      ,anio,mes,dia);
    datePickerDialog.show();
  }

  private void Establecer_Telefono_Contacto(){
    CountryCodePicker ccp;
    EditText Establecer_Telefono;
    Button Btn_Aceptar_Telefono;


    dialog_actualizar_telefono.setContentView(R.layout.cuadro_dialogo_establecer_telefono);

    ccp = dialog_actualizar_telefono.findViewById(R.id.ccp);
    Establecer_Telefono = dialog_actualizar_telefono.findViewById(R.id.Establecer_Telefono);
    Btn_Aceptar_Telefono = dialog_actualizar_telefono.findViewById(R.id.Btn_Aceptar_Telefono);

    Btn_Aceptar_Telefono.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String codigo_pais = ccp.getSelectedCountryCodeWithPlus();
        String telefono = Establecer_Telefono.getText().toString();
        String codigo_pais_telefono = codigo_pais+telefono; //+52 99334000000

        if (!telefono.equals("")) {
          Telefono_C_A.setText(codigo_pais_telefono);
          dialog_actualizar_telefono.dismiss();
        }else{
          //Toast.makeText(Agregar_Contacto.this, "", Toast.LENGTH_SHORT).show();
          dialog_actualizar_telefono.dismiss();
        }
      }
    });

    dialog_actualizar_telefono.show();
    dialog_actualizar_telefono.setCanceledOnTouchOutside(true);
  }

  private void ActualizarInformacionContacto(){

    String NombresActualizar = Nombres_C_A.getText().toString().trim();
    String ApellidosActualizar = Apellidos_C_A.getText().toString().trim();
    String CorreActualizar = Correo_C_A.getText().toString().trim();
    String TelefonoActualizar = Telefono_C_A.getText().toString().trim();
    String Fecha_Nac_Actualizar = Fecha_Nacimiento_C_A.getText().toString().trim();
    String EdadActualizar = Edad_C_A.getText().toString().trim();
    String InstitucionActualizar = Institucion_C_A.getText().toString().trim();
    String DireccionActualizar = Direccion_C_A.getText().toString().trim();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Usuarios");

    Query query = databaseReference.child(user.getUid()).child("Contactos").orderByChild("id_contacto").equalTo(id_c);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot ds : snapshot.getChildren()){
          ds.getRef().child("nombres").setValue(NombresActualizar);
          ds.getRef().child("apellidos").setValue(ApellidosActualizar);
          ds.getRef().child("correo").setValue(CorreActualizar);
          ds.getRef().child("telefono").setValue(TelefonoActualizar);
          ds.getRef().child("fec_nacimiento").setValue(Fecha_Nac_Actualizar);
          ds.getRef().child("edad").setValue(EdadActualizar);
          ds.getRef().child("institucion").setValue(InstitucionActualizar);
          ds.getRef().child("direccion").setValue(DireccionActualizar);

        }
        Toast.makeText(Actualizar_Contacto.this, "Contacto actualizado", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

  }

  private void SubirImagenStorage() {
    progressDialog.setMessage("Subiendo imagen");
    progressDialog.show();

    String id_c = getIntent().getStringExtra("id_c");

    String carpetaImagenesContactos = "ImagenesPerfilContacto/";
    String NombreImagen = carpetaImagenesContactos + id_c;

    StorageReference reference = FirebaseStorage.getInstance().getReference(NombreImagen);
    reference.putFile(imagenUri)
      .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
          Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
          while (!uriTask.isSuccessful()) ;
          String uriImagen = "" + uriTask.getResult();
          ActualizarImagenBD(uriImagen);
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Toast.makeText(Actualizar_Contacto.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

  }

  private void ActualizarImagenBD(String uriImagen) {
    progressDialog.setMessage("Actualizando imagen");
    progressDialog.show();

    String id_c = getIntent().getStringExtra("id_c");

    HashMap<String, Object> hashMap = new HashMap<>();
    if (imagenUri != null) {
      hashMap.put("imagen", "" + uriImagen);
    }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
    databaseReference.child(user.getUid()).child("Contactos").child(id_c)
      .updateChildren(hashMap)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void unused) {
          progressDialog.dismiss();
          Toast.makeText(Actualizar_Contacto.this, "Imagen actualizada con exito", Toast.LENGTH_SHORT).show();
          onBackPressed();
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Toast.makeText(Actualizar_Contacto.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

  }



  private void SeleccionarImagenGaleria() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    galeriaActivityResultLauncher.launch(intent);

  }

  private ActivityResultLauncher<Intent> galeriaActivityResultLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    new ActivityResultCallback<ActivityResult>() {
      @Override
      public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
          //Obtener uri de la imagen
          Intent data = result.getData();
          imagenUri = data.getData();

          //Setear la imagen
          Imagen_C_A.setImageURI(imagenUri);
          SubirImagenStorage();

        } else {
          Toast.makeText(Actualizar_Contacto.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
        }
      }
    }
  );

  //Permiso para acceder a la galeria
  private ActivityResultLauncher<String> SolicitudPermisoGaleria = registerForActivityResult(
    new ActivityResultContracts.RequestPermission(), isGranted -> {
      if (isGranted) {
        SeleccionarImagenGaleria();
      } else {
        Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
      }

    });


  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return super.onSupportNavigateUp();
  }

}
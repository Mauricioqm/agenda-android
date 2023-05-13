package com.example.agenda.ActualizarNota;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Actualizar_Nota extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

  TextView Id_nota_A, Uid_Usuario_A, Correo_Usuario_A, Fecha_registro_A, Fecha_A, Estado_A, Estado_nuevo;
  EditText Titulo_A, Descripcion_A;
  Button Btn_calenadrio_A;

  String id_nota_R, uid_Usuario_R, correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

  ImageView Tarea_Finalizada, Tarea_No_Finalizada;

  Spinner Spinner_estado;

  int dia, mes, anio;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actualizar_nota);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle("Actualizar nota");
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);

    InicializarVistas();
    RecuperarDatos();
    SetearDatos();
    ComprobarEstadoNota();
    Spinner_Estado();

    Btn_calenadrio_A.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SeleccionarFecha();
      }
    });
  }
  private void InicializarVistas(){
    Id_nota_A = findViewById(R.id.Id_nota_A);
    Uid_Usuario_A = findViewById(R.id.Uid_Usuario_A);
    Correo_Usuario_A = findViewById(R.id.Correo_Usuario_A);
    Fecha_registro_A = findViewById(R.id.Fecha_registro_A);
    Titulo_A = findViewById(R.id.Titulo_A);
    Descripcion_A = findViewById(R.id.Descripcion_A);
    Fecha_A = findViewById(R.id.Fecha_A);
    Estado_A = findViewById(R.id.Estado_A);
    Btn_calenadrio_A = findViewById(R.id.Btn_calenadrio_A);

    Tarea_Finalizada = findViewById(R.id.Tarea_Finalizada);
    Tarea_No_Finalizada = findViewById(R.id.Tarea_No_Finalizada);

    Spinner_estado = findViewById(R.id.Spinner_estado);
    Estado_nuevo = findViewById(R.id.Estado_nuevo);
  }

  private void RecuperarDatos(){
    Bundle intent = getIntent().getExtras();
    id_nota_R = intent.getString("id_nota");
    uid_Usuario_R = intent.getString("uid_usuario");
    correo_usuario_R = intent.getString("corre_usuario");
    fecha_registro_R = intent.getString("fechas_registro");
    titulo_R = intent.getString("titulo");
    descripcion_R = intent.getString("descripcion");
    fecha_R = intent.getString("fecha_nota");
    estado_R = intent.getString("estado");
  }

  private void SetearDatos(){
    Id_nota_A.setText(id_nota_R);
    Uid_Usuario_A.setText(uid_Usuario_R);
    Correo_Usuario_A.setText(correo_usuario_R);
    Fecha_registro_A.setText(fecha_registro_R);
    Titulo_A.setText(titulo_R);
    Descripcion_A.setText(descripcion_R);
    Fecha_A.setText(fecha_R);
    Estado_A.setText(estado_R);
  }

  private void ComprobarEstadoNota(){
    String estado_nota = Estado_A.getText().toString();
    if(estado_nota.equals("No finalizado")){
      Tarea_No_Finalizada.setVisibility(View.VISIBLE);
    }
    if (estado_nota.equals("Finalizado")){
      Tarea_Finalizada.setVisibility(View.VISIBLE);
    }
  }

  private void SeleccionarFecha(){
    final Calendar calendar = Calendar.getInstance();

    dia = calendar.get(Calendar.DAY_OF_MONTH);
    mes = calendar.get(Calendar.MONTH);
    anio = calendar.get(Calendar.YEAR);

    DatePickerDialog datePickerDialog = new DatePickerDialog(Actualizar_Nota.this, new DatePickerDialog.OnDateSetListener() {
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
        Fecha_A.setText(diaF + "/" + mesF + "/" +anioS);

      }
    }
      ,anio,mes,dia);
    datePickerDialog.show();
  }


  private void Spinner_Estado(){
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
      R.array.Estado_nota, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    Spinner_estado.setAdapter(adapter);
    Spinner_estado.setOnItemSelectedListener(this);
  }

  private void ActualizarNotaBD(){
    String tituloActualizar = Titulo_A.getText().toString();
    String descripcionActualizar = Descripcion_A.getText().toString();
    String fechaActualizar = Fecha_A.getText().toString();
    String estadoActualizar = Estado_nuevo.getText().toString();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Notas_Publicadas");

    //Consulta
    Query query = databaseReference.orderByChild("id_nota").equalTo(id_nota_R);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot ds : snapshot.getChildren()){
          ds.getRef().child("titulo").setValue(tituloActualizar);
          ds.getRef().child("descripcion").setValue(descripcionActualizar);
          ds.getRef().child("fecha_nota").setValue(fechaActualizar);
          ds.getRef().child("estado").setValue(estadoActualizar);
        }
        Toast.makeText(Actualizar_Nota.this, "Nota actualizada con exito", Toast.LENGTH_SHORT).show();
        onBackPressed();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
    String ESTADO_ACTUAL = Estado_A.getText().toString();

    String Posicion_1 = adapterView.getItemAtPosition(1).toString();

    String estado_seleccionado = adapterView.getItemAtPosition(position).toString();
    Estado_nuevo.setText(estado_seleccionado);

    if(ESTADO_ACTUAL.equals("Finalizado")){
      Estado_nuevo.setText(Posicion_1);
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_actualizar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()){
      case R.id.Actualizar_Nota_BD:
        ActualizarNotaBD();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return super.onSupportNavigateUp();
  }
}
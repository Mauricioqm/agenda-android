package com.example.agenda.AgregarNota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.Objetos.Nota;
import com.example.agenda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Agregar_Nota extends AppCompatActivity {

  TextView Uid_Usuario, Correo_Usuario, Fecha_hora_actual, Fecha, Estado;
  EditText Titulo, Descripcion;
  Button Btn_calenadrio;

  int dia, mes, anio;

  DatabaseReference BD_Firebase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_agregar_nota);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle("");
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);

    InicializarVariables();
    ObtenerDatos();
    Obtener_Fecha_Hora_Actual();

    Btn_calenadrio.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final Calendar calendar = Calendar.getInstance();

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Agregar_Nota.this, new DatePickerDialog.OnDateSetListener() {
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
            Fecha.setText(diaF + "/" + mesF + "/" +anioS);

          }
        }
          ,anio,mes,dia);
        datePickerDialog.show();
      }
    });
  }

  private void InicializarVariables(){

    Uid_Usuario = findViewById(R.id.Uid_Usuario);
    Correo_Usuario = findViewById(R.id.Correo_Usuario);
    Fecha_hora_actual = findViewById(R.id.Fecha_hora_actual);
    Fecha = findViewById(R.id.Fecha);
    Estado = findViewById(R.id.Estado);

    Titulo = findViewById(R.id.Titulo);
    Descripcion = findViewById(R.id.Descripcion);
    Btn_calenadrio = findViewById(R.id.Btn_calenadrio);

    BD_Firebase = FirebaseDatabase.getInstance().getReference();
  }

  private void ObtenerDatos(){
    String uid_recuperado = getIntent().getStringExtra("Uid");
    String correo_recuperado = getIntent().getStringExtra("Correo");

    Uid_Usuario.setText(uid_recuperado);
    Correo_Usuario.setText(correo_recuperado);
  }

  private void Obtener_Fecha_Hora_Actual(){
    String Fecha_hora_registro = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a",
      Locale.getDefault()).format(System.currentTimeMillis());
    //Ejemplo: 13-11-2022/10:10:20 pm

    Fecha_hora_actual.setText(Fecha_hora_registro);

  }

  private void Agrega_Nota(){

    //Obtener los datos
    String uid_usuario = Uid_Usuario.getText().toString();
    String correo_usario = Correo_Usuario.getText().toString();
    String fecha_hora_actual = Fecha_hora_actual.getText().toString();
    String titulo = Titulo.getText().toString();
    String descripcion = Descripcion.getText().toString();
    String fecha = Fecha.getText().toString();
    String estado = Estado.getText().toString();
    String id_nota = BD_Firebase.push().getKey();

    //Validar datos
    if(!uid_usuario.equals("") && !correo_usario.equals("") && !fecha_hora_actual.equals("") &&
      !titulo.equals("") && !descripcion.equals("") && !fecha.equals("") && !estado.equals("")){

      Nota nota = new Nota(id_nota,
        uid_usuario,
        correo_usario,
        fecha_hora_actual,
        titulo,
        descripcion,
        fecha,
        estado);


      //Establecer el nombre de la BD
      String Nombre_BD = "Notas_Publicadas";

      BD_Firebase.child(Nombre_BD).child(id_nota).setValue(nota);
      Toast.makeText(this, "¡Se agrego la nota existosamente!", Toast.LENGTH_SHORT).show();
      onBackPressed();

    }else{
      Toast.makeText(this, "Llenar todos los campos", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_agregar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()){
      case R.id.Agregar_Nota_BD:
        Agrega_Nota();
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
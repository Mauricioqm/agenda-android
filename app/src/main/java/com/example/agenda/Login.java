package com.example.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

  EditText CorreoLogin, PassLoging;
  TextView UsuarionuevoTXT;
  Button Btn_Logeo;

  ProgressDialog progressDialog;

  FirebaseAuth firebaseAuth;

  //Validar Datos
  String correo = "", password = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle("Login");
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);

    CorreoLogin = findViewById(R.id.CorreoLogin);
    PassLoging = findViewById(R.id.PassLoging);
    UsuarionuevoTXT = findViewById(R.id.UsuarionuevoTXT);
    Btn_Logeo = findViewById(R.id.Btn_Logeo);

    firebaseAuth = FirebaseAuth.getInstance();

    progressDialog = new ProgressDialog(Login.this);
    progressDialog.setTitle("Espere por favor");
    progressDialog.setCanceledOnTouchOutside(false);

    Btn_Logeo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ValidarDatos();
      }
    });

    UsuarionuevoTXT.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(Login.this, Registro.class));
      }
    });
  }

  private void ValidarDatos() {

    correo = CorreoLogin.getText().toString();
    password = PassLoging.getText().toString();

    if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
      Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
    }else if (TextUtils.isEmpty(password)){
      Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
    }else {
      LoginDeUsuario();
    }
  }

  private void LoginDeUsuario() {
    progressDialog.setMessage("Iniciando sesión...");
    progressDialog.show();

    firebaseAuth.signInWithEmailAndPassword(correo,password)
      .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

          if(task.isSuccessful()){
            progressDialog.dismiss();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            startActivity(new Intent(Login.this, MenuPrincipal.class));
            Toast.makeText(Login.this, "Bienvenid@: "+user.getDisplayName(), Toast.LENGTH_LONG).show();
            finish();
          }
          else {
            progressDialog.dismiss();
            Toast.makeText(Login.this, "Verifique si las credenciales son correctas...", Toast.LENGTH_SHORT).show();
          }
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return super.onSupportNavigateUp();
  }
}
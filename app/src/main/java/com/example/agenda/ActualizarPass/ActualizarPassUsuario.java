package com.example.agenda.ActualizarPass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.Login;
import com.example.agenda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ActualizarPassUsuario extends AppCompatActivity {

  TextView ActualPass;
  EditText EtActualPass, EtNuevoPass, EtRNuevoPass;

  Button BtnActualizarPass;

  DatabaseReference BD_Usuarios;
  FirebaseAuth firebaseAuth;
  FirebaseUser user;

  ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actualizar_pass_usuario);

    ActionBar actionBar = getSupportActionBar();
    assert actionBar != null;
    actionBar.setTitle("Cambiar contraseña");
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);


    IniciliazarVariables();
    LecturaDeDatos();

    BtnActualizarPass.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String Pass_Actual = EtActualPass.getText().toString().trim();
        String Nuevo_Pass = EtNuevoPass.getText().toString().trim();
        String RNuevo_Pass = EtRNuevoPass.getText().toString().trim();

        if(Pass_Actual.equals("")){
          EtActualPass.setError("Llene el campo");
        }else if(Nuevo_Pass.equals("")){
          EtNuevoPass.setError("Llene el campo");
        }else if(RNuevo_Pass.equals("")){
          EtRNuevoPass.setError("Llene el campo");
        }
        else if(!Nuevo_Pass.equals(RNuevo_Pass)){
          Toast.makeText(ActualizarPassUsuario.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
        else if(Nuevo_Pass.length()<6){
          EtNuevoPass.setError("Debe ser igual o mayor a 6 caracteres");
        }else{
          Actulizar_Password(Pass_Actual, Nuevo_Pass);
        }

      }
    });
  }

  private void IniciliazarVariables(){
    ActualPass = findViewById(R.id.ActualPass);
    EtActualPass = findViewById(R.id.EtActualPass);
    EtNuevoPass = findViewById(R.id.EtNuevoPass);
    EtRNuevoPass = findViewById(R.id.EtRNuevoPass);
    BtnActualizarPass = findViewById(R.id.BtnActualizarPass);

    firebaseAuth = FirebaseAuth.getInstance();
    user = firebaseAuth.getCurrentUser();

    progressDialog = new ProgressDialog(ActualizarPassUsuario.this);

  }

  private void LecturaDeDatos(){
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
    reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        String pass = ""+snapshot.child("password").getValue();
        ActualPass.setText(pass);

      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

  }

  private void Actulizar_Password(String pass_actual, String nuevo_pass) {
    progressDialog.show();
    progressDialog.setTitle("Actualizar");
    progressDialog.setMessage("Espere por favor");

    AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), pass_actual);
    user.reauthenticate(authCredential)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void unused) {
          user.updatePassword(nuevo_pass)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void unused) {
                progressDialog.dismiss();
                String Nuevo_pass = EtNuevoPass.getText().toString().trim();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("password",Nuevo_pass);
                BD_Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
                BD_Usuarios.child(user.getUid()).updateChildren(hashMap)
                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                      Toast.makeText(ActualizarPassUsuario.this, "Contraseña actualizada con exito", Toast.LENGTH_SHORT).show();
                      firebaseAuth.signOut();
                      Intent intent = new Intent(ActualizarPassUsuario.this, Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(intent);
                      finish();
                    }
                  }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                      progressDialog.dismiss();
                      Toast.makeText(ActualizarPassUsuario.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                  });

              }
            }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ActualizarPassUsuario.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
              }
            });
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          progressDialog.dismiss();
          Toast.makeText(ActualizarPassUsuario.this, "Contraseña actual no es correcta", Toast.LENGTH_SHORT).show();
        }
      });

  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return super.onSupportNavigateUp();
  }
}
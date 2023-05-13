package com.example.agenda.ListarNotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.agenda.ActualizarNota.Actualizar_Nota;
import com.example.agenda.Detalle.Detalle_Nota;
import com.example.agenda.Objetos.Nota;
import com.example.agenda.R;
import com.example.agenda.ViewHolder.ViewHolder_Nota;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Listar_Notas extends AppCompatActivity {

  RecyclerView recyclerviewNotas;

  FirebaseDatabase firebaseDatabase;
  DatabaseReference BASE_DE_DATOS;

  LinearLayoutManager linearLayoutManager;

  FirebaseRecyclerAdapter<Nota, ViewHolder_Nota> firebaseRecyclerAdapter;
  FirebaseRecyclerOptions<Nota> options;

  Dialog dialog;

  FirebaseAuth auth;
  FirebaseUser user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_listar_notas);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle("Mis notas");
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);

    recyclerviewNotas = findViewById(R.id.recyclerviewNotas);
    recyclerviewNotas.setHasFixedSize(true);

    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();

    firebaseDatabase = FirebaseDatabase.getInstance();
    BASE_DE_DATOS = firebaseDatabase.getReference("Notas_Publicadas");
    dialog = new Dialog(Listar_Notas.this);

    ListarNotasUsuarios();
  }

  private void ListarNotasUsuarios(){
    //Consulta
    Query query = BASE_DE_DATOS.orderByChild("uid_usuario").equalTo(user.getUid());

    //query se cambia por BASE_DE_DATOS para poder listar TODAS las notas
    options = new FirebaseRecyclerOptions.Builder<Nota>().setQuery(query, Nota.class).build();
    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nota, ViewHolder_Nota>(options) {
      @Override
      protected void onBindViewHolder(@NonNull ViewHolder_Nota viewHolder_nota , int position, @NonNull Nota nota) {
        viewHolder_nota.SetearDatos(
          getApplicationContext(),
          nota.getId_nota(),
          nota.getUid_usuario(),
          nota.getCorreo_usuario(),
          nota.getFechas_hora_actual(),
          nota.getTitulo(),
          nota.getDescripcion(),
          nota.getFecha_nota(),
          nota.getEstado()
        );
      }

      @NonNull
      @Override
      public ViewHolder_Nota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota,parent,false);
        ViewHolder_Nota viewHolder_nota = new ViewHolder_Nota(view);
        viewHolder_nota.setOnClickListerer(new ViewHolder_Nota.ClickListener() {
          @Override
          public void onItemClick(View view, int position) {
            //startActivity(new Intent(Listar_Notas.this, Detalle_Nota.class));

            //Obtener los datos de la nota seleccionada
            String id_nota = getItem(position).getId_nota();
            String uid_usuario = getItem(position).getUid_usuario();
            String corre_usuario = getItem(position).getCorreo_usuario();
            String fechas_registro = getItem(position).getFechas_hora_actual();
            String titulo = getItem(position).getTitulo();
            String descripcion = getItem(position).getDescripcion();
            String fecha_nota = getItem(position).getFecha_nota();
            String estado = getItem(position).getEstado();

            //Enviando los datos a la siguiente Actividad
            Intent intent = new Intent(Listar_Notas.this, Detalle_Nota.class);
            intent.putExtra("id_nota", id_nota);
            intent.putExtra("uid_usuario", uid_usuario);
            intent.putExtra("corre_usuario", corre_usuario);
            intent.putExtra("fechas_registro", fechas_registro);
            intent.putExtra("titulo", titulo);
            intent.putExtra("descripcion", descripcion);
            intent.putExtra("fecha_nota", fecha_nota);
            intent.putExtra("estado", estado);
            startActivity(intent);
          }

          @Override
          public void onItemLongClick(View view, int position) {

            //Obtener los datos de la nota seleccionada
            String id_nota = getItem(position).getId_nota();
            String uid_usuario = getItem(position).getUid_usuario();
            String corre_usuario = getItem(position).getCorreo_usuario();
            String fechas_registro = getItem(position).getFechas_hora_actual();
            String titulo = getItem(position).getTitulo();
            String descripcion = getItem(position).getDescripcion();
            String fecha_nota = getItem(position).getFecha_nota();
            String estado = getItem(position).getEstado();


            //Declarar las vistas
            Button CD_Eliminar, CD_Actualizar;

            //Realizar la conexion con el diseño
            dialog.setContentView(R.layout.dialogo_opciones);

            //Inicializar las vistas
            CD_Eliminar = dialog.findViewById(R.id.CD_Eliminar);
            CD_Actualizar = dialog.findViewById(R.id.CD_Actualizar);

            CD_Eliminar.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                EliminarNota(id_nota);
                dialog.dismiss();

              }
            });

            CD_Actualizar.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                //Toast.makeText(Listar_Notas.this, "Actualizar Nota", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(Listar_Notas.this, Actualizar_Nota.class));
                Intent intent = new Intent(Listar_Notas.this, Actualizar_Nota.class);
                intent.putExtra("id_nota", id_nota);
                intent.putExtra("uid_usuario", uid_usuario);
                intent.putExtra("corre_usuario", corre_usuario);
                intent.putExtra("fechas_registro", fechas_registro);
                intent.putExtra("titulo", titulo);
                intent.putExtra("descripcion", descripcion);
                intent.putExtra("fecha_nota", fecha_nota);
                intent.putExtra("estado", estado);
                startActivity(intent);
                dialog.dismiss();

              }
            });
            dialog.show();
          }
        });

        return viewHolder_nota;
      }
    };

    linearLayoutManager = new LinearLayoutManager(Listar_Notas.this, LinearLayoutManager.VERTICAL, false);
    linearLayoutManager.setReverseLayout(true);
    linearLayoutManager.setStackFromEnd(true);

    recyclerviewNotas.setLayoutManager(linearLayoutManager);
    recyclerviewNotas.setAdapter(firebaseRecyclerAdapter);

  }

  private void EliminarNota(String id_nota) {
    AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Notas.this);
    builder.setTitle("Eliminar nota");
    builder.setMessage("¿Desea eliminar la nota?");
    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //Crear Consulta para eliminar nota
        Query query = BASE_DE_DATOS.orderByChild("id_note").equalTo(id_nota);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot ds : snapshot.getChildren()){
              ds.getRef().removeValue();
            }
            Toast.makeText(Listar_Notas.this, "Nota eliminada", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(Listar_Notas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
          }
        });
      }
    });

    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(Listar_Notas.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
      }
    });

    builder.create().show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_vaciar_todas_las_notas, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if(item.getItemId() == R.id.Vaciar_Todas_las_Notas){
      Vaciar_Registro_De_Notas();
    }
    return super.onOptionsItemSelected(item);
  }

  private void Vaciar_Registro_De_Notas() {
    AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Notas.this);
    builder.setTitle("Vaciar todos los registros");
    builder.setMessage("¿Esta segur@ de eliminar todas las notas?");

    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //Elimiacion de todas las notas
        Query query = BASE_DE_DATOS.orderByChild("uid_usuario").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
              ds.getRef().removeValue();
            }
            Toast.makeText(Listar_Notas.this, "Todas las notas fueron eliminadas correctamente", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
        });
      }
    });
    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(Listar_Notas.this, "Accion Cancelada", Toast.LENGTH_SHORT).show();
      }
    });
    builder.create().show();
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (firebaseRecyclerAdapter!=null){
      firebaseRecyclerAdapter.startListening();
    }
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return super.onSupportNavigateUp();
  }
}
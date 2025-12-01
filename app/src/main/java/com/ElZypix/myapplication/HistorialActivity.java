package com.ElZypix.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elzypix.clinometro.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import androidx.appcompat.widget.SearchView;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView rvHistorial;
    private HistorialAdapter adapter;
    private ArrayList<Logica_registros> listaProyectos;
    private SearchView svBuscador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // 1. Encontrar el RecyclerView en el diseño
        rvHistorial = findViewById(R.id.rvHistorial);

        // 2. Configurar cómo se ven los elementos (Lista vertical)
        rvHistorial.setLayoutManager(new LinearLayoutManager(this));

        // 3. Cargar los datos guardados
        cargarDatos();

        // 4. Crear el Adapter y dárselo al RecyclerView
        // (Le pasamos la lista que acabamos de cargar)
        adapter = new HistorialAdapter(listaProyectos);
        adapter.setOnItemLongClickListener(new HistorialAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                // Cuando ocurra el clic largo, llamamos a nuestro diálogo
                mostrarDialogoBorrar(position);
            }
        });
        rvHistorial.setAdapter(adapter);


        // Configurar el buscador
        svBuscador = findViewById(R.id.svBuscador);
        svBuscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Cada vez que escribes una letra, llamamos a filtrar
                filtrar(newText);
                return true;
            }
        });
    }


    private void cargarDatos() {
        SharedPreferences sharedPreferences = getSharedPreferences("Mis_Preferencias", MODE_PRIVATE);
        Gson gson = new Gson();

        // Leer el texto JSON
        String json = sharedPreferences.getString("Lista_proyectos", null);

        // Definir el tipo de lista para Gson
        Type type = new TypeToken<ArrayList<Logica_registros>>() {}.getType();

        // Convertir Texto -> Lista
        if (json == null) {
            // Si no había nada guardado, iniciamos lista vacía para que no falle
            listaProyectos = new ArrayList<>();
            Toast.makeText(this, "No hay proyectos guardados aún", Toast.LENGTH_SHORT).show();
        } else {
            listaProyectos = gson.fromJson(json, type);
        }
    }

    public void regresarAlMenu(View view) {
        finish();
    }
    private void filtrar(String texto) {
        ArrayList<Logica_registros> listaFiltrada = new ArrayList<>();

        // Recorremos la lista ORIGINAL (listaProyectos)
        for (Logica_registros proyecto : listaProyectos) {
            // Si el nombre del proyecto contiene lo que escribimos...
            if (proyecto.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                // ...lo añadimos a la lista filtrada
                listaFiltrada.add(proyecto);
            }
        }

        // Le enviamos la lista filtrada al adaptador
        adapter.filtrarLista(listaFiltrada);
    }
    private void mostrarDialogoBorrar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Proyecto");
        builder.setMessage("¿Estás seguro de que quieres borrar este proyecto?");

        builder.setPositiveButton("Sí, borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarProyecto(position);
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    private void borrarProyecto(int position) {
        // 1. Borrar de la lista en memoria RAM
        listaProyectos.remove(position);

        // 2. Avisar al adaptador para que quite la fila visualmente con una animación
        adapter.notifyItemRemoved(position);

        // 3. GUARDAR la nueva lista (sin el elemento) en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Mis_Preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonActualizado = gson.toJson(listaProyectos);
        editor.putString("Lista_proyectos", jsonActualizado);
        editor.apply();

        Toast.makeText(this, "Proyecto eliminado", Toast.LENGTH_SHORT).show();
    }
}
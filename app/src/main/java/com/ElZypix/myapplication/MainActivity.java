package com.ElZypix.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Context;
import android.content.SharedPreferences;

import com.elzypix.clinometro.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.view.inputmethod.InputMethodManager;
import android.content.DialogInterface;
import android.widget.EditText;
import android.text.InputType;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText etxDistancia, etxAngulo, etxAlturaOjos;
    private TextView txvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicializo los elementos de tipo EditText
        etxDistancia = findViewById(R.id.etxDistancia);
        etxAngulo = findViewById(R.id.etxAngulo2);
        etxAlturaOjos = findViewById(R.id.etxAlturaOjos);

        //Inicializo los elementos de tipo TextView
        txvResultado = findViewById(R.id.txvResultado);

        Intent RecibirDatos = getIntent();
        if (RecibirDatos != null && RecibirDatos.hasExtra("Dato_distancia")){
            double distanciaRecibida = RecibirDatos.getDoubleExtra("Dato_distancia", 0.0);
            etxDistancia.setText(String.format("%.5f", distanciaRecibida).replace(",", "."));
        }

    }

    public void CalcularAltura(View view){
        double Distancia, Angulo, AlturaOjos;
        String strDistancia, strAngulo, strAlturaOjo;
        strDistancia = etxDistancia.getText().toString();
        strAngulo = etxAngulo.getText().toString();
        strAlturaOjo = etxAlturaOjos.getText().toString();

        if (strDistancia.isEmpty() && strAngulo.isEmpty() && strAlturaOjo.isEmpty()){
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
            return;
        }
        if(strDistancia.isEmpty()){
            Toast.makeText(this, "La distancia no puedes estar vacia", Toast.LENGTH_SHORT).show();
            etxDistancia.requestFocus();
            return;
        }
        if(strAngulo.isEmpty()){
            Toast.makeText(this, "El angulo no puede estar vacio", Toast.LENGTH_SHORT).show();
            etxAngulo.requestFocus();
            return;
        }
        if(strAlturaOjo.isEmpty()){
            Toast.makeText(this, "La altura de tus ojos no puede estar vacia", Toast.LENGTH_SHORT).show();
            etxAlturaOjos.requestFocus();
            return;
        }

        Distancia = Double.parseDouble(strDistancia);
        Angulo = Double.parseDouble(strAngulo);
        AlturaOjos = Double.parseDouble(strAlturaOjo);

        if (Distancia <= 0){
            Toast.makeText(this, "La distancia debe de ser mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Angulo <= 0 || Angulo >= 90){
            Toast.makeText(this, "El angulo debe de ser entre 1 y 89 grados", Toast.LENGTH_SHORT).show();
            return;
        }
        if (AlturaOjos <= 0){
            Toast.makeText(this, "La altura de los ojos debe de ser mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }
        double x = Distancia * Math.tan(Math.toRadians(Angulo));
        double AlturaTotal = x + AlturaOjos;
        txvResultado.setText("La Altura del edificio es: " + String.format("%.5f", AlturaTotal) + " metros");
    }
    public void LimpiarCampos(View view){
        etxDistancia.setText("");
        etxAlturaOjos.setText("");
        etxAngulo.setText("");
        txvResultado.setText("La Altura del edificio es: __.__ metros");
        etxDistancia.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null){
            imm.showSoftInput(etxDistancia, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public void paginaPrincipal(View view){
        finish();
    }
    public void mostrarDialogoGuardar(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nombre del proyecto");

        final EditText input = new EditText(this);
        input.setHint("Ej. Edificio centro");
        builder.setView(input);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombreProyecto = input.getText().toString();
                if (!nombreProyecto.isEmpty()) {
                    GuardarProyecto(nombreProyecto);
                }
                Toast.makeText(getApplicationContext(), "Guardando: "+ nombreProyecto, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    private void GuardarProyecto(String nombre){
        double distancia = Double.parseDouble(etxDistancia.getText().toString());
        double angulo = Double.parseDouble(etxAngulo.getText().toString());
        double alturaOjos = Double.parseDouble(etxAlturaOjos.getText().toString());

        double x = distancia * Math.tan(Math.toRadians(angulo));
        double alturaTotal = x + alturaOjos;

        Logica_registros nuevoRegistro = new Logica_registros(nombre, distancia, angulo, alturaOjos, alturaTotal);

        SharedPreferences sharedPreferences = getSharedPreferences("Mis_Preferencias", MODE_PRIVATE);
        Gson gson = new Gson();

        String jsonActual = sharedPreferences.getString("Lista_proyectos", null);
        Type type = new TypeToken<ArrayList<Logica_registros>>() {}.getType();
        ArrayList<Logica_registros> listaProyectos;

        if (jsonActual == null){
            listaProyectos = new ArrayList<>();
        }else{
            listaProyectos = gson.fromJson(jsonActual, type);
        }
        listaProyectos.add(nuevoRegistro);

        String jsonActualizado = gson.toJson(listaProyectos);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Lista_proyectos", jsonActualizado);
        editor.apply();

        Toast.makeText(this, "Proyecto guardado correctamente", Toast.LENGTH_SHORT).show();
    }
}
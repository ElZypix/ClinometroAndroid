package com.ElZypix.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SegundaVentana extends AppCompatActivity {

    EditText etxAltura, etxAngulo;
    TextView txvResultado2;
    double DistanciaTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_segunda_ventana);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etxAltura = findViewById(R.id.etxAltura);
        etxAngulo = findViewById(R.id.etxAngulo2);
        txvResultado2 = findViewById(R.id.txvResultado2);
    }

    public void CalcularDistancia(View view){
        String Altura = etxAltura.getText().toString();
        String Angulo = etxAngulo.getText().toString();

        if(Altura.isEmpty() && Angulo.isEmpty()){
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Altura.isEmpty()){
            Toast.makeText(this, "La altura no puede estar vacia", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Angulo.isEmpty()){
            Toast.makeText(this, "El angulo no puede estar vacio", Toast.LENGTH_SHORT).show();
            return;
        }

        double h = Double.parseDouble(Altura);
        double a = Double.parseDouble(Angulo);

        if (h <= 0){
            Toast.makeText(this, "La altura debe de ser mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (a <= 0 || a >= 90){
            Toast.makeText(this, "El angulo debe de ser entre 1 y 89 grados", Toast.LENGTH_SHORT).show();
            return;
        }

        DistanciaTotal = h * Math.tan(Math.toRadians(a));
        txvResultado2.setText("La distancia es de " + String.format("%.5f", DistanciaTotal) + " metros");
    }
    public void LimparCampos(View view){
        etxAltura.setText("");
        etxAngulo.setText("");
        txvResultado2.setText("La distancia es de: __.__ metros");
        etxAltura.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(etxAltura, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public void EnviarDatos(View view){
        if (DistanciaTotal == 0){
            Toast.makeText(this, "Debes de calcular la distancia primero", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Dato_distancia", DistanciaTotal);
        startActivity(intent);
        finish();
    }

}
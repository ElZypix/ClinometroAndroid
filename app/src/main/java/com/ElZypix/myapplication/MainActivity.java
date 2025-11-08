package com.ElZypix.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText etxDistancia, etxAngulo, etxAlturaOjos;
    private Button btnCalcular;
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
        etxAngulo = findViewById(R.id.etxAngulo);
        etxAlturaOjos = findViewById(R.id.etxAlturaOjos);

        //Inicializo los elementos de tipo Button
        btnCalcular = findViewById(R.id.btnCalcular);

        //Inicializo los elementos de tipo TextView
        txvResultado = findViewById(R.id.txvResultado);

    }

    public void CalcularAltura(View view){
        try {
            double Distancia, Angulo, AlturaOjos;
            Distancia = Double.parseDouble(etxDistancia.getText().toString());
            Angulo = Double.parseDouble(etxAngulo.getText().toString());
            AlturaOjos = Double.parseDouble(etxAlturaOjos.getText().toString());

            double x = Distancia * Math.tan(Math.toRadians(Angulo));
            double AlturaTotal = x + AlturaOjos;
            txvResultado.setText("La Altura del edificio es: " + String.format("%.2f", AlturaTotal) + " metros");
        }catch (NumberFormatException e){
            Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
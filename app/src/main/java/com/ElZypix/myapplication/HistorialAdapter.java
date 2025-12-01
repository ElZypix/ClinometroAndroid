package com.ElZypix.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elzypix.clinometro.R;

import java.util.ArrayList;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ProjectViewHolder> {

    private ArrayList<Logica_registros> listaProyectos;

    // NUEVO 1: Variable para el Listener (la antena)
    private OnItemLongClickListener listener;

    // NUEVO 2: Definimos la Interfaz
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    // NUEVO 3: Método para conectar la antena desde fuera
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.listener = listener;
    }

    public HistorialAdapter(ArrayList<Logica_registros> lista) {
        this.listaProyectos = lista;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyecto, parent, false);
        // Pasamos el listener al ViewHolder
        return new ProjectViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Logica_registros proyectoActual = listaProyectos.get(position);
        holder.tvNombre.setText(proyectoActual.getNombre());
        String textoAltura = String.format("Altura: %.5f m", proyectoActual.getAlturaTotal());
        holder.tvAltura.setText(textoAltura);
    }

    @Override
    public int getItemCount() {
        return listaProyectos.size();
    }

    public void filtrarLista(ArrayList<Logica_registros> listaFiltrada) {
        this.listaProyectos = listaFiltrada;
        notifyDataSetChanged();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvAltura;

        // NUEVO 4: Modificamos el constructor para recibir el listener
        public ProjectViewHolder(@NonNull View itemView, final OnItemLongClickListener listener) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProyecto);
            tvAltura = itemView.findViewById(R.id.tvAlturaCalculada);

            // NUEVO 5: Detectamos el Clic Largo
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(position);
                            return true; // Consumimos el evento (para que no haga clic normal también)
                        }
                    }
                    return false;
                }
            });
        }
    }
}
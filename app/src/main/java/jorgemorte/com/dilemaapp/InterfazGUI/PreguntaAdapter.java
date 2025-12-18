package jorgemorte.com.dilemaapp.InterfazGUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jorgemorte.com.dilemaapp.modelo.PartidaActual;
import jorgemorte.com.dilemaapp.modelo.Pregunta;
import jorgemorte.com.dilemaapp.R;

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder> {
    private List<Pregunta> preguntas;
    private String modoJuego;

    public PreguntaAdapter(List<Pregunta> preguntas, String modoJuego) {
        this.preguntas = preguntas;
        this.modoJuego = modoJuego;
    }

    @NonNull
    @Override
    public PreguntaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new PreguntaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreguntaViewHolder holder, int position) {
        Pregunta pregunta = preguntas.get(position);
        holder.gameTitulo.setText(pregunta.getTexto());

        if (PartidaActual.gameId == 5) {
            holder.txtActivarTabu.setVisibility(View.VISIBLE);
            holder.gamePreguntas.setVisibility(View.VISIBLE);
            holder.gamePreguntas.setText(pregunta.getPalabrasTaboo().replace(",","\n\n").toUpperCase());
        } else {
            holder.txtActivarTabu.setVisibility(View.GONE);

            holder.gamePreguntas.setVisibility(View.VISIBLE);
            //mayusculas
            holder.gamePreguntas.setText(pregunta.getPalabrasTaboo().toUpperCase());
            //mas grande el texto
            holder.gamePreguntas.setTextSize(16);
            //separacion de lineas
            holder.gamePreguntas.setLineSpacing(0, 1.6f);
        }
    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    static class PreguntaViewHolder extends RecyclerView.ViewHolder {
        TextView gameTitulo, gamePreguntas, txtActivarTabu;

        public PreguntaViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitulo = itemView.findViewById(R.id.gameTitulo);
            gamePreguntas = itemView.findViewById(R.id.gamePreguntas);
            txtActivarTabu = itemView.findViewById(R.id.txtActivarTabu);
        }
    }
}


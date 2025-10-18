package jorgemorte.com.dilemaapp.InterfazGUI;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jorgemorte.com.dilemaapp.DB.JugadorHelper;
import jorgemorte.com.dilemaapp.R;
import jorgemorte.com.dilemaapp.modelo.PartidaActual;
import jorgemorte.com.dilemaapp.modelo.Player;

public class VentanaInicio extends AppCompatActivity {

    private EditText txtNombreJugador;
    private Button btnAgregar;
    private LinearLayout listaJugadores;  // Contenedor para la lista visual de jugadores
    private JugadorHelper jugadorHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ventana_inicio);

        txtNombreJugador = findViewById(R.id.txtNombreJugador);
        btnAgregar = findViewById(R.id.btnAgregar);
        Button btnJugar = findViewById(R.id.btnJugar);
        listaJugadores = findViewById(R.id.listaJugadores);

        jugadorHelper = new JugadorHelper(this);

        btnAgregar.setOnClickListener(v -> agregarJugador());

        // Cargar y mostrar la lista de jugadores existentes al iniciar la Activity
        cargarJugadoresEnLista();

        btnJugar.setOnClickListener (new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                //ir a la ventana selecionar juego
                List <Player> jugadoresSelecionados= jugadorHelper.getAllJugadores();
                PartidaActual.jugadores = jugadoresSelecionados;

                Intent intent = new Intent(VentanaInicio.this, VentanaSelecionarJuego.class);
                startActivity(intent);
            }

        });
    }



    private void cargarJugadoresEnLista() {
        listaJugadores.removeAllViews(); // limpia la lista antes de cargar
        List<Player> jugadores = jugadorHelper.getAllJugadores();
        for (Player jugador : jugadores) {
            agregarJugadorALaLista(jugador.getNombre());
        }
    }

    private void agregarJugador() {
        String nombre = txtNombreJugador.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        if (jugadorHelper.existeJugador(nombre)) {
            mostrarDialogoJugadorExiste(nombre);
        } else {
            crearJugador(nombre);
        }
    }

    private void crearJugador(String nombre) {
        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Player nuevoJugador = new Player(nombre, fecha);
        jugadorHelper.insertarJugador(nuevoJugador);
        txtNombreJugador.setText("");
        agregarJugadorALaLista(nombre);
        Toast.makeText(this, "Jugador agregado: " + nombre, Toast.LENGTH_SHORT).show();
    }

    private void mostrarDialogoJugadorExiste(String nombre) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Jugador ya existe");
        builder.setMessage("Ya hay un jugador con ese nombre. ¿Qué deseas hacer?");

        builder.setPositiveButton("Continuar", (dialog, which) -> {
            txtNombreJugador.setText("");
            Toast.makeText(this, "Continuando con: " + nombre, Toast.LENGTH_SHORT).show();
            // Aquí podrías iniciar la partida o algo similar con el jugador existente
        });

        builder.setNegativeButton("Reemplazar", (dialog, which) -> {
            jugadorHelper.eliminarJugador(nombre);
            eliminarJugadorDeLista(nombre);
            crearJugador(nombre);
            agregarJugadorALaLista(nombre);
        });

        builder.setNeutralButton("Cambiar nombre", (dialog, which) -> {
            txtNombreJugador.requestFocus();
        });

        builder.show();
    }

    private void agregarJugadorALaLista(String nombre) {
        LinearLayout filaJugador = new LinearLayout(this);
        filaJugador.setOrientation(LinearLayout.HORIZONTAL);
        filaJugador.setBackgroundResource(R.drawable.linear_bordered_background);
        filaJugador.setPadding(14, 14, 14, 14);

        LinearLayout.LayoutParams paramsFila = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramsFila.setMargins(0, 8, 0, 8);
        filaJugador.setLayoutParams(paramsFila);

        TextView nombreJugador = new TextView(this);
        nombreJugador.setText(nombre);
        nombreJugador.setTextSize(22f);
        nombreJugador.setTypeface(Typeface.DEFAULT_BOLD);
        nombreJugador.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        nombreJugador.setPadding(14, 0, 14, 0);

        Button btnEliminar = new Button(this);
        btnEliminar.setText("-");
        btnEliminar.setTextColor(Color.BLACK);
        btnEliminar.setTypeface(Typeface.DEFAULT_BOLD);
        btnEliminar.setBackgroundResource(R.drawable.btn_circle_orange);
        btnEliminar.setGravity(Gravity.CENTER);

        // Tamaño circular: 48dp x 48dp
        int sizeInDp = (int) (48 * getResources().getDisplayMetrics().density); // convierte 48dp a px
        LinearLayout.LayoutParams paramsBtn = new LinearLayout.LayoutParams(sizeInDp, sizeInDp);
        paramsBtn.gravity = Gravity.CENTER_VERTICAL;
        btnEliminar.setLayoutParams(paramsBtn);

        btnEliminar.setOnClickListener(v -> {
            listaJugadores.removeView(filaJugador);
            jugadorHelper.eliminarJugador(nombre);
            Toast.makeText(VentanaInicio.this, "Jugador eliminado: " + nombre, Toast.LENGTH_SHORT).show();
        });

        filaJugador.addView(nombreJugador);
        filaJugador.addView(btnEliminar);

        listaJugadores.addView(filaJugador);
    }


private void eliminarJugadorDeLista(String nombre) {
        int count = listaJugadores.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = listaJugadores.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout fila = (LinearLayout) child;
                View primerHijo = fila.getChildAt(0);
                if (primerHijo instanceof TextView) {
                    TextView nombreJugador = (TextView) primerHijo;
                    if (nombreJugador.getText().toString().equals(nombre)) {
                        listaJugadores.removeView(fila);
                        break;
                    }
                }
            }
        }
    }


}

package jorgemorte.com.dilemaapp.InterfazGUI;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import net.sqlcipher.database.SQLiteDatabase;

import jorgemorte.com.dilemaapp.DB.DBHelper;
import jorgemorte.com.dilemaapp.R;
import jorgemorte.com.dilemaapp.modelo.PartidaActual;

public class Games extends Fragment {

    // Clave de la base de datos definida
    private static final String DB_PASSWORD = "1234";
    private LinearLayout gameListContainer;
    private LayoutInflater inflater;
    private DBHelper dbHelper;

    public Games() {
        // Constructor vacío
    }

    // Factory method to create a new instance of the fragment
    public static Games newInstance(String param1, String param2) {
        Games fragment = new Games();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Aquí podrías obtener los parámetros si los necesitas
            String param1 = getArguments().getString("param1");
            String param2 = getArguments().getString("param2");
        }

        // Inicializar el DBHelper y el LayoutInflater
        dbHelper = new DBHelper(getActivity());
        inflater = LayoutInflater.from(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragment
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        // Configurar el contenedor de juegos
        gameListContainer = view.findViewById(R.id.gameListContainer);

        // Cargar los juegos desde la base de datos
        cargarJuegosDesdeDB();

        // Configurar el botón "Volver"
        //btnVolver.setOnClickListener(v -> {
            // Llamar a getActivity() para acceder a la actividad que contiene el fragmento
        //    if (getActivity() != null) {
        //         getActivity().finish();  // Terminar la actividad actual
        //    }
        // });

        return view;
    }

    private void cargarJuegosDesdeDB() {
        // Inicializar variables para asegurar el cierre
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getEncryptedWritableDatabase(DB_PASSWORD);

            if (db == null) {
                Log.e("DB_GAME_LOAD", "Fallo al abrir DB para cargar juegos.");
                Toast.makeText(getActivity(), "Error: No se pudo conectar a la base de datos.", Toast.LENGTH_LONG).show();
                return;
            }

            cursor = db.rawQuery("SELECT * FROM TipoJuego", null);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));  // Obtener el ID
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

                // Inflar la vista de la tarjeta de juego
                View card = inflater.inflate(R.layout.item_game_card, gameListContainer, false);

                TextView title = card.findViewById(R.id.gameTitle);
                TextView desc = card.findViewById(R.id.gameDescription);
                ImageView icon = card.findViewById(R.id.gameIcon);

                title.setText(nombre);
                desc.setText(descripcion);
                icon.setImageResource(R.drawable.ic_action_name);  // Asegúrate de que el recurso exista

                // Guardar el id en la tarjeta para recuperarlo luego
                card.setTag(id);

                // Poner listener para detectar clic en la tarjeta
                card.setOnClickListener(v -> {
                    int juegoIdSeleccionado = (int) v.getTag();

                    Toast.makeText(getActivity(), "Juego seleccionado con ID: " + juegoIdSeleccionado, Toast.LENGTH_SHORT).show();

                    // Guardar en PartidaActual
                    PartidaActual.gameId = juegoIdSeleccionado;
                    Intent intent = new Intent(getActivity(), VentanaSeleccionarModo.class);
                    startActivity(intent);
                });

                gameListContainer.addView(card);
            }
        } catch (Exception e) {
            Log.e("DB_GAME_LOAD", "Error al cargar juegos.", e);
            Toast.makeText(getActivity(), "Error al leer datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            // Asegurar el cierre de recursos
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
}

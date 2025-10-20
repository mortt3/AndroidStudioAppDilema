package jorgemorte.com.dilemaapp.InterfazGUI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Si usas CardView
import androidx.cardview.widget.CardView;

import net.sqlcipher.database.SQLiteDatabase; // Importación necesaria para el objeto DB de SQLCipher

import jorgemorte.com.dilemaapp.DB.DBHelper;
import jorgemorte.com.dilemaapp.R;
import jorgemorte.com.dilemaapp.modelo.PartidaActual;

public class VentanaSelecionarJuego extends AppCompatActivity {
    LinearLayout gameListContainer;
    LayoutInflater inflater;
    DBHelper dbHelper;

    // Clave de la base de datos definida
    private static final String DB_PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_selecionar_juego);
        ImageButton btnVolver= findViewById(R.id.btnVolver);
        gameListContainer = findViewById(R.id.gameListContainer);
        inflater = LayoutInflater.from(this);
        dbHelper = new DBHelper(this);
        cargarJuegosDesdeDB();

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dispose  esta pagina y volver a la ventana de inicio
                finish();
            }
        });
    }

    private void cargarJuegosDesdeDB() {
        // Inicializar variables para asegurar el cierre
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // ⭐ USAMOS EL MÉTODO getEncryptedWritableDatabase CORREGIDO
            db = dbHelper.getEncryptedWritableDatabase(DB_PASSWORD);

            if (db == null) {
                Log.e("DB_GAME_LOAD", "Fallo al abrir DB para cargar juegos.");
                Toast.makeText(this, "Error: No se pudo conectar a la base de datos.", Toast.LENGTH_LONG).show();
                return;
            }

            cursor = db.rawQuery("SELECT * FROM TipoJuego", null);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));  // Obtener el ID
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

                View card = inflater.inflate(R.layout.item_game_card, gameListContainer, false);

                TextView title = card.findViewById(R.id.gameTitle);
                TextView desc = card.findViewById(R.id.gameDescription);
                ImageView icon = card.findViewById(R.id.gameIcon);

                title.setText(nombre);
                desc.setText(descripcion);

                icon.setImageResource(R.drawable.ic_action_name);

                // Guardar el id en la tarjeta para recuperarlo luego
                card.setTag(id);

                // Poner listener para detectar clic en la tarjeta
                card.setOnClickListener(v -> {
                    int juegoIdSeleccionado = (int) v.getTag();
                    // Aquí haces lo que necesites con el id seleccionado, ejemplo:
                    Toast.makeText(this, "Juego seleccionado con ID: " + juegoIdSeleccionado, Toast.LENGTH_SHORT).show();

                    // Guardar en PartidaActual si quieres
                    PartidaActual.gameId = juegoIdSeleccionado;

                    // Luego puedes pasar a la siguiente pantalla, por ejemplo:
                    Intent intent = new Intent(VentanaSelecionarJuego.this, VentanaSeleccionarModo.class);
                    startActivity(intent);
                });

                gameListContainer.addView(card);
            }
        } catch (Exception e) {
            Log.e("DB_GAME_LOAD", "Error al cargar juegos.", e);
            Toast.makeText(this, "Error al leer datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
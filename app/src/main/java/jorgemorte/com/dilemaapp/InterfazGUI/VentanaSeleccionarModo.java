package jorgemorte.com.dilemaapp.InterfazGUI;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.sqlcipher.database.SQLiteDatabase; // Importación necesaria para el objeto DB

import jorgemorte.com.dilemaapp.DB.DBHelper;
import jorgemorte.com.dilemaapp.R;
import jorgemorte.com.dilemaapp.modelo.PartidaActual;

public class VentanaSeleccionarModo extends AppCompatActivity {
    private DBHelper dbHelper;
    private LinearLayout gameListContainer;
    private LayoutInflater inflater;

    // Clave de la base de datos definida
    private static final String DB_PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_seleccionar_modo);
        inflater = LayoutInflater.from(this);
        gameListContainer = findViewById(R.id.gameListContainer);
        dbHelper = new DBHelper(this);
        cargarModoDesdeDB();
        ImageButton btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             finish();
                                         }
                                     }
        );

    }

    private void cargarModoDesdeDB() {
        // Inicializar variables para asegurar el cierre
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // ⭐ USAMOS EL MÉTODO getEncryptedWritableDatabase CORREGIDO
            db = dbHelper.getEncryptedWritableDatabase(DB_PASSWORD);

            if (db == null) {
                Log.e("DB_MODE_LOAD", "Fallo al abrir DB para cargar modos.");
                Toast.makeText(this, "Error: No se pudo conectar a la base de datos.", Toast.LENGTH_LONG).show();
                return;
            }

            cursor = db.rawQuery("SELECT * FROM ModoJuego", null);

            while (cursor.moveToNext()) {
                // int id = cursor.getInt(cursor.getColumnIndexOrThrow("id")); // No usado actualmente, se puede omitir
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

                View card = inflater.inflate(R.layout.item_mode_card, gameListContainer, false);

                TextView title = card.findViewById(R.id.gameTitle);
                TextView desc = card.findViewById(R.id.gameDescription);
                ImageView icon = card.findViewById(R.id.gameIcon);

                title.setText(nombre);
                desc.setText(descripcion);

                icon.setImageResource(R.drawable.ic_action_name);

                // Guardar el nombre del modo en la tarjeta para recuperarlo luego
                card.setTag(nombre);

                // Poner listener para detectar clic en la tarjeta
                card.setOnClickListener(v -> {
                    String modoNombreSeleccionado = (String) v.getTag();
                    // Aquí haces lo que necesites con el id seleccionado, ejemplo:
                    Toast.makeText(this, "Modo seleccionado: " + modoNombreSeleccionado, Toast.LENGTH_SHORT).show();

                    // Guardar en PartidaActual si quieres
                    PartidaActual.modo = modoNombreSeleccionado;

                    // Luego  pasar a la siguiente pantalla
                    Intent intent = new Intent(VentanaSeleccionarModo.this, VentanaJuego.class);
                    startActivity(intent);
                });

                gameListContainer.addView(card);
            }
        } catch (Exception e) {
            Log.e("DB_MODE_LOAD", "Error al cargar modos de juego.", e);
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
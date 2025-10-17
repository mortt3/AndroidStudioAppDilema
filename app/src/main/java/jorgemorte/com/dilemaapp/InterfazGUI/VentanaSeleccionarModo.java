package jorgemorte.com.dilemaapp.InterfazGUI;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import jorgemorte.com.dilemaapp.DB.DBHelper;
import jorgemorte.com.dilemaapp.R;
import jorgemorte.com.dilemaapp.modelo.PartidaActual;

public class VentanaSeleccionarModo extends AppCompatActivity {
    private DBHelper dbHelper;
    private LinearLayout gameListContainer;
    private LayoutInflater inflater;
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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ModoJuego", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

            View card = inflater.inflate(R.layout.item_mode_card, gameListContainer, false);

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
                String modoNombreSeleccionado = (String) v.getTag();
                // Aquí haces lo que necesites con el id seleccionado, ejemplo:
                Toast.makeText(this, "Modo seleccionado: " + modoNombreSeleccionado, Toast.LENGTH_SHORT).show();

                // Guardar en PartidaActual si quieres
                PartidaActual.modo = modoNombreSeleccionado;

                // Luego  pasar a la siguiente pantalla
                //



            });

            gameListContainer.addView(card);
        }

        cursor.close();
        db.close();
    }
}
package jorgemorte.com.dilemaapp.InterfazGUI;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Si usas CardView
import androidx.cardview.widget.CardView;


import jorgemorte.com.dilemaapp.DB.DBHelper;
import jorgemorte.com.dilemaapp.R;

public class VentanaSelecionarJuego extends AppCompatActivity {
    LinearLayout gameListContainer;
    LayoutInflater inflater;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_selecionar_juego);

        gameListContainer = findViewById(R.id.gameListContainer);
        inflater = LayoutInflater.from(this);
        dbHelper = new DBHelper(this);

        cargarJuegosDesdeDB();
    }

    private void cargarJuegosDesdeDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TipoJuego", null);

        while (cursor.moveToNext()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

            View card = inflater.inflate(R.layout.item_game_card, gameListContainer, false);

            TextView title = card.findViewById(R.id.gameTitle);
            TextView desc = card.findViewById(R.id.gameDescription);
            ImageView icon = card.findViewById(R.id.gameIcon);

            title.setText(nombre);
            desc.setText(descripcion);

            // Puedes usar lógica para cambiar el ícono según el nombre del juego
            icon.setImageResource(R.drawable.ic_action_name);

            // Agregar la tarjeta al contenedor
            gameListContainer.addView(card);
        }

        cursor.close();
        db.close();
    }

}
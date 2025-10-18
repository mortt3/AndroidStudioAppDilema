package jorgemorte.com.dilemaapp.InterfazGUI;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import jorgemorte.com.dilemaapp.DB.DBHelper;
import jorgemorte.com.dilemaapp.modelo.PartidaActual;
import jorgemorte.com.dilemaapp.modelo.Pregunta;
import jorgemorte.com.dilemaapp.R;

import android.widget.Toast;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

public class VentanaJuego extends AppCompatActivity {
    private DBHelper dbHelper;
    private CardStackView cardStackView;
    private PreguntaAdapter adapter;
    private CardStackLayoutManager manager;
    private List<Pregunta> listaPreguntas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_juego);

        dbHelper = new DBHelper(this);
        cardStackView = findViewById(R.id.card_stack_view); // Cambia recyclerPreguntas por card_stack_view

        TextView txtTitulo = findViewById(R.id.txtJuego);
        txtTitulo.setText(obtenerNombreJuego());

        cargarPreguntasDesdeDB();

        // Configuramos el CardStackLayoutManager para controlar swipe
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardSwiped(Direction direction) {
                if (manager.getTopPosition() == adapter.getItemCount()) {
                    Toast.makeText(VentanaJuego.this, "Se acabaron las preguntas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override public void onCardDragging(Direction direction, float ratio) {}
            @Override public void onCardRewound() {}
            @Override public void onCardCanceled() {}
            @Override public void onCardAppeared(View view, int position) {}
            @Override public void onCardDisappeared(View view, int position) {}
        });

        // Opciones para que el swipe sea horizontal y la pila de cartas se vea bien
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);

        adapter = new PreguntaAdapter(listaPreguntas, PartidaActual.modo);

        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
    }

    private String obtenerNombreJuego() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String nombreJuego = "Juego no encontrado";

        String query = "SELECT nombre FROM TipoJuego WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(PartidaActual.gameId)});

        if (cursor.moveToFirst()) {
            nombreJuego = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
        }

        cursor.close();
        db.close();

        return nombreJuego;
    }

    private void cargarPreguntasDesdeDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Dilema WHERE game_id = ? AND estiloJuego = ? AND activo = 1";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(PartidaActual.gameId), PartidaActual.modo});

        listaPreguntas.clear();

        while (cursor.moveToNext()) {
            String texto = cursor.getString(cursor.getColumnIndexOrThrow("palabra_clave"));
            String palabrasTaboo = cursor.getString(cursor.getColumnIndexOrThrow("palabras_tabú"));

            Pregunta pregunta = new Pregunta(texto, palabrasTaboo);
            listaPreguntas.add(pregunta);
        }

        cursor.close();
        db.close();
    }
}


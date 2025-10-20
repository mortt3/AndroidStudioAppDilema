package jorgemorte.com.dilemaapp.InterfazGUI;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.sqlcipher.database.SQLiteDatabase; // Importación de SQLiteDatabase de SQLCipher

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.List;

import jorgemorte.com.dilemaapp.DB.DBHelper;
import jorgemorte.com.dilemaapp.modelo.PartidaActual;
import jorgemorte.com.dilemaapp.modelo.Pregunta;
import jorgemorte.com.dilemaapp.R;

public class VentanaJuego extends AppCompatActivity {
    private DBHelper dbHelper;
    private CardStackView cardStackView;
    private PreguntaAdapter adapter;
    private CardStackLayoutManager manager;
    private List<Pregunta> listaPreguntas = new ArrayList<>();

    // Clave definida a nivel de clase para reuso
    private static final String DB_PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_juego);

        dbHelper = new DBHelper(this);
        cardStackView = findViewById(R.id.card_stack_view); // Cambia recyclerPreguntas por card_stack_view

        TextView txtTitulo = findViewById(R.id.txtJuego);
        txtTitulo.setText(obtenerNombreJuego()); // Obtener el nombre del juego

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
        net.sqlcipher.database.SQLiteDatabase db = null;
        Cursor cursor = null;
        String nombreJuego = "Juego no encontrado";

        try {
            // ⭐ USAMOS EL MÉTODO getEncryptedWritableDatabase CORREGIDO
            db = dbHelper.getEncryptedWritableDatabase(DB_PASSWORD);

            if (db == null) {
                Log.e("DB_TITLE", "Fallo al abrir DB para obtener nombre del juego.");
                return nombreJuego;
            }

            String query = "SELECT nombre FROM TipoJuego WHERE id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(PartidaActual.gameId)});

            if (cursor.moveToFirst()) {
                nombreJuego = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            }

        } catch (Exception e) {
            Log.e("DB_TITLE", "Error al obtener el nombre del juego.", e);
        } finally {
            // Asegurar el cierre de recursos
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return nombreJuego;
    }

    private void cargarPreguntasDesdeDB() {
        // La clave ya está definida a nivel de clase
        net.sqlcipher.database.SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // ⭐ 1. Llamada al método corregido y seguro:
            db = dbHelper.getEncryptedWritableDatabase(DB_PASSWORD);

            if (db == null) {
                Log.e("DB_LOAD", "Fallo de conexión: Clave incorrecta o base no copiada.");
                return;
            }

            String query = "SELECT palabra_clave, palabras_tabú FROM Dilema WHERE game_id = ? AND estiloJuego = ? AND activo = 1";

            // 2. Ejecutar la consulta
            cursor = db.rawQuery(query, new String[]{
                    String.valueOf(PartidaActual.gameId),
                    PartidaActual.modo
            });

            listaPreguntas.clear();

            // 3. Procesar resultados
            while (cursor.moveToNext()) {
                String texto = cursor.getString(cursor.getColumnIndexOrThrow("palabra_clave"));
                String palabrasTaboo = cursor.getString(cursor.getColumnIndexOrThrow("palabras_tabú"));

                Pregunta pregunta = new Pregunta(texto, palabrasTaboo);
                listaPreguntas.add(pregunta);
            }
            Log.d("DB_LOAD", "Preguntas cargadas exitosamente: " + listaPreguntas.size());

        } catch (Exception e) {
            Log.e("DB_LOAD", "Error al cargar preguntas o consultar la base de datos.", e);

        } finally {
            // 4. Asegurar el cierre de recursos
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
}


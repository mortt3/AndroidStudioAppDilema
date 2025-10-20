package jorgemorte.com.dilemaapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import jorgemorte.com.dilemaapp.modelo.Player; // Asegúrate de que esta ruta sea correcta

import java.util.ArrayList;
import java.util.List;

public class JugadorHelper {

    private static final String TAG = "JugadorHelper";
    // ⚠️ ATENCIÓN: Esta clave DEBE coincidir con la usada para cifrar MiBase.db
    private static final String DB_PASSWORD = "1234";

    private SQLiteDatabase db;
    private final DBHelper dbHelper;

    // 1. Constructor: Intenta abrir la DB
    public JugadorHelper(Context context) {
        this.dbHelper = new DBHelper(context);
        try {
            // Utilizamos el método que incluye la lógica de copiado y apertura con clave
            db = dbHelper.getEncryptedWritableDatabase(DB_PASSWORD);
            if (db == null) {
                // Esto solo se ejecuta si la apertura falló (clave incorrecta, archivo corrupto, o error de copia)
                Log.e(TAG, "ERROR CRÍTICO: No se pudo abrir la base de datos cifrada. Revisar logs anteriores.");
                // Podrías lanzar una RuntimeException aquí para detener la aplicación si es un fallo fatal
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar JugadorHelper y abrir la DB.", e);
            db = null; // Aseguramos que la DB quede como null si falla
        }
    }

    // --- Métodos de Verificación y Lectura ---

    public boolean existeJugador(String nombre) {
        if (db == null) return false;
        Cursor cursor = null;
        try {
            // Usamos rawQuery para mayor compatibilidad con SQLCipher
            cursor = db.rawQuery("SELECT nombre FROM Jugador WHERE nombre = ?", new String[]{nombre});
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(TAG, "Error al verificar la existencia del jugador: " + nombre, e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public Player getJugador(String nombre) {
        if (db == null) return null;
        Cursor cursor = null;
        Player jugador = null;
        try {
            cursor = db.rawQuery("SELECT nombre, fechaUltimaVezJugado FROM Jugador WHERE nombre = ?", new String[]{nombre});
            if (cursor.moveToFirst()) {
                // Usamos getColumnIndexOrThrow para evitar errores de columna inexistente
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fechaUltimaVezJugado"));
                jugador = new Player(nombre, fecha);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener el jugador: " + nombre, e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return jugador;
    }

    public List<Player> getAllJugadores() {
        List<Player> jugadores = new ArrayList<>();
        if (db == null) return jugadores;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT nombre, fechaUltimaVezJugado FROM Jugador", null);

            if (cursor.moveToFirst()) {
                do {
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fechaUltimaVezJugado"));
                    jugadores.add(new Player(nombre, fecha));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener todos los jugadores.", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return jugadores;
    }

    // --- Métodos de Escritura (CRUD) ---

    public void insertarJugador(Player jugador) {
        if (db == null) return;
        try {
            ContentValues values = new ContentValues();
            values.put("nombre", jugador.getNombre());
            values.put("fechaUltimaVezJugado", jugador.getFechaUltimaPartida());

            // Usamos 'db.insert()' de SQLCipher
            long id = db.insert("Jugador", null, values);
            if (id == -1) {
                Log.e(TAG, "Fallo al insertar el jugador: " + jugador.getNombre());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al insertar el jugador: " + jugador.getNombre(), e);
        }
    }

    public void eliminarJugador(String nombre) {
        if (db == null) return;
        try {
            // Usamos 'db.delete()' de SQLCipher
            int filasAfectadas = db.delete("Jugador", "nombre = ?", new String[]{nombre});
            Log.d(TAG, "Jugador eliminado: " + nombre + ". Filas afectadas: " + filasAfectadas);
        } catch (Exception e) {
            Log.e(TAG, "Error al eliminar el jugador: " + nombre, e);
        }
    }

    // --- Cierre de Recursos ---

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        // No es necesario cerrar dbHelper aquí ya que solo contiene la referencia
    }
}



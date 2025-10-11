package jorgemorte.com.dilemaapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jorgemorte.com.dilemaapp.modelo.Player;

public class JugadorHelper {
    private SQLiteDatabase db;

    public JugadorHelper(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean existeJugador(String nombre) {
        Cursor cursor = db.rawQuery("SELECT * FROM Jugador WHERE nombre = ?", new String[]{nombre});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    public Player getJugador(String nombre) {
        Cursor cursor = db.rawQuery("SELECT * FROM Jugador WHERE nombre = ?", new String[]{nombre});
        if (cursor.moveToFirst()) {
            String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fechaUltimaVezJugado"));
            cursor.close();
            return new Player(nombre, fecha);
        }
        cursor.close();
        return null;
    }

    public void insertarJugador(Player jugador) {
        ContentValues values = new ContentValues();
        values.put("nombre", jugador.getNombre());
        values.put("fechaUltimaVezJugado", jugador.getFechaUltimaPartida());
        db.insert("Jugador", null, values);
    }

    public void eliminarJugador(String nombre) {
        db.delete("Jugador", "nombre = ?", new String[]{nombre});
    }
    public List<Player> getAllJugadores() {
        List<Player> jugadores = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT nombre, fechaUltimaVezJugado FROM Jugador", null);

        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fechaUltimaVezJugado"));
                jugadores.add(new Player(nombre, fecha));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return jugadores;
    }
}


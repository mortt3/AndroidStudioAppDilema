package jorgemorte.com.dilemaapp.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartidaActual {
    public static int gameId;
    public static String modo;
    public static List<Player> jugadores = new ArrayList<>();
    public static int turnoActual = 0;
    public static Map<String, Integer> puntuaciones = new HashMap<>();  // nombre → puntos

    public static void reiniciar() {
        turnoActual = 0;
        puntuaciones.clear();
        for (Player p : jugadores) {
            puntuaciones.put(p.getNombre(), 0);
        }
    }

    public static Player getJugadorActual() {
        if (jugadores.isEmpty()) return null;
        return jugadores.get(turnoActual % jugadores.size());
    }

    public static void sumarPunto(String nombre) {
        int puntos = puntuaciones.getOrDefault(nombre, 0);
        puntuaciones.put(nombre, puntos + 1);
    }

    public static void siguienteTurno() {
        turnoActual++;
    }
}


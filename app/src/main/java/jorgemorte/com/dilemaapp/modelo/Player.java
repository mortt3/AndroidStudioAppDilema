package jorgemorte.com.dilemaapp.modelo;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import java.time.LocalDate;

import java.time.LocalDate;
import java.util.Locale;

public class Player {
    private String nombre;
    private String fechaUltimaPartida;

    public Player(String nombre, String fechaUltimaPartida) {
        this.nombre = validarNombre(nombre);
        this.fechaUltimaPartida = fechaUltimaPartida;
    }
    private String validarNombre(String nombre) {
        if (nombre == null || nombre.isEmpty() ) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        } else if (!nombre.matches("[a-zA-Z0-9ñáéíóúÁÉÍÓÚñÑ: \\\"-_ç() ]{1,50}")){
            throw new IllegalArgumentException("El nombre contiene caracteres no válidos o es demasiado corto");
        }
        return nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFechaUltimaPartida() {
        return fechaUltimaPartida;
    }
}

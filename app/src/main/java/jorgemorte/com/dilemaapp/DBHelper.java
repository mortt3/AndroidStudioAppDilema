package jorgemorte.com.dilemaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MiBase.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creamos las tablas
        db.execSQL("CREATE TABLE TipoJuego (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "descripcion TEXT, " +
                "tiempo_default INTEGER)");

        db.execSQL("CREATE TABLE Dilema (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "palabra_clave TEXT, " +           // La palabra que deben adivina y su es reto o verdad
                "palabras_tabú TEXT, " +           // Palabras prohibidas separadas por comas o el reto
                "game_id INTEGER, " +              // FK a TipoJuego
                "estiloJuego TEXT, " +             // Soft / fiesta / hot
                "activo INTEGER DEFAULT 1, " +    // si está disponible o no
                "FOREIGN KEY (game_id) REFERENCES TipoJuego(id))");


        db.execSQL("CREATE TABLE Jugador (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "fechaUltimaVezJugado TEXT" +
                ")");

        db.execSQL("CREATE TABLE Partida (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "jugador_id INTEGER, " +
                "game_id INTEGER, " +
                "fecha TEXT, " +
                "puntuacion INTEGER, " +
                "FOREIGN KEY (jugador_id) REFERENCES Jugador(id), " +
                "FOREIGN KEY (game_id) REFERENCES TipoJuego(id))");

        db.execSQL("INSERT INTO TipoJuego (nombre, descripcion, tiempo_default) VALUES " +
                "('Verdad o Reto', 'Juego clásico donde debes elegir entre responder una verdad o hacer un reto.', 60), " +
                "('Preguntas Locas', 'Juego con preguntas divertidas y curiosas para animar el ambiente.', 45), " +
                "('Trivia', 'Juego de preguntas y respuestas para poner a prueba tus conocimientos.', 30), " +
                "('Adivina la Película', 'Desafía a tus amigos adivinando títulos de películas con pistas.', 50), " +
                "('Taboo', 'Compite en equipos haciendo que tu equipo adivine palabras sin decir las prohibidas o tabú.', 90);");

        // Verdad o Reto (game_id = 1)
        db.execSQL("INSERT INTO Dilema (palabra_clave, palabras_tabú, game_id, estiloJuego, activo) VALUES " +
                "('¿Qué es lo más loco o estúpido que has hecho por amor (o por un crush)?', '', 1, 'soft', 1), " +
                "('¿La mentira más gorda que has contado a tus padres y que nunca descubrieron?', '', 1, 'soft', 1), " +
                "('¿De qué estás secretamente más orgulloso/a, aunque pueda parecer raro?', '', 1, 'soft', 1), " +
                "('¿Cuál es tu mayor miedo completamente irracional o inconfesable?', '', 1, 'soft', 1), " +
                "('¿Alguna vez has tenido un crush (aunque sea pequeño) con alguien de esta sala?', '', 1, 'fiesta', 1), " +
                "('¿Cuál es el sueño más raro o vergonzoso que has tenido últimamente?', '', 1, 'fiesta', 1), " +
                "('Lee en voz alta el último mensaje que intercambiaste con tu crush / tu novio/a / tu ex más reciente.', '', 1, 'hot', 1), " +
                "('Deja que la persona a tu derecha elija una foto vergonzosa de la galería de tu móvil y enséñasela al grupo.', '', 1, 'fiesta', 1), " +
                "('Llama a uno de tus padres e intenta hacerle cr   eer una historia un poco loca durante al menos 1 minuto.', '', 1, 'soft', 1), " +
                "('Haz una declaración de amistad muy intensa a la persona que elijas.', '', 1, 'fiesta', 1), " +
                "('Envía un mensaje de voz a tu crush cantando el estribillo de una canción de amor cursi.', '', 1, 'hot', 1), " +
                "('Enseña tu historial de búsqueda de YouTube o Spotify de los últimos 5 vídeos vistos.', '', 1, 'fiesta', 1);");


        // Preguntas Locas (game_id = 2)
        db.execSQL("INSERT INTO Dilema (palabra_clave, palabras_tabú, game_id, estiloJuego, activo) VALUES " +
                "('Si pudieras ser un animal por un día, ¿cuál serías y por qué?', '', 2, 'soft', 1), " +
                "('¿Qué superpoder elegirías si solo lo tuvieras por 24 horas?', '', 2, 'soft', 1), " +
                "('¿Cuál sería el título de una película sobre tu vida?', '', 2, 'fiesta', 1), " +
                "('Si pudieras cenar con cualquier personaje histórico o ficticio, ¿quién sería?', '', 2, 'soft', 1), " +
                "('¿Qué harías si te despertaras invisible durante un día?', '', 2, 'fiesta', 1);");


        // Trivia (game_id = 3)
        db.execSQL("INSERT INTO Dilema (palabra_clave, palabras_tabú, game_id, estiloJuego, activo) VALUES " +
                "('¿Cuál es el planeta más grande del sistema solar?', 'Júpiter', 3, 'soft', 1), " +
                "('¿En qué año llegó el hombre a la Luna?', '1969', 3, 'soft', 1), " +
                "('¿Qué país inventó la pizza?', 'Italia', 3, 'soft', 1), " +
                "('¿Cuántos colores tiene el arcoíris?', '7', 3, 'fiesta', 1), " +
                "('¿Quién pintó la Mona Lisa?', 'Leonardo da Vinci', 3, 'soft', 1);");


        // Adivina la Película (game_id = 4)
        db.execSQL("INSERT INTO Dilema (palabra_clave, palabras_tabú, game_id, estiloJuego, activo) VALUES " +
                "('Titanic', 'barco, iceberg, hundimiento, amor, Jack', 4, 'soft', 1), " +
                "('El Rey León', 'Simba, Mufasa, selva, león, Disney', 4, 'soft', 1), " +
                "('Harry Potter', 'mago, varita, Hogwarts, hechizo, escuela', 4, 'fiesta', 1), " +
                "('Frozen', 'hielo, Elsa, Anna, nieve, cantar', 4, 'soft', 1), " +
                "('Jurassic Park', 'dinosaurio, isla, parque, T-Rex, clones', 4, 'fiesta', 1);");


        // Taboo (game_id = 5)
        db.execSQL("INSERT INTO Dilema (palabra_clave, palabras_tabú, game_id, estiloJuego, activo) VALUES " +
                "('Montaña', 'trepar, subir, roca, cima, nieve', 5, 'soft', 1), " +
                "('Mar', 'agua, playa, ola, sal, barco', 5, 'soft', 1), " +
                "('Escuela', 'profesor, alumno, clase, estudiar, tarea', 5, 'soft', 1), " +
                "('Perro', 'animal, mascota, ladrar, cola, hueso', 5, 'soft', 1), " +
                "('Teléfono', 'llamada, móvil, hablar, número, mensaje', 5, 'soft', 1);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Partida");
        db.execSQL("DROP TABLE IF EXISTS Dilema");
        db.execSQL("DROP TABLE IF EXISTS TipoJuego");
        onCreate(db);
    }
}


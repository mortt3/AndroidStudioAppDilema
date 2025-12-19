package jorgemorte.com.dilemaapp.DB;

import android.content.Context;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "MiBase.db";
    private static final int DATABASE_VERSION = 2;
    private final Context context;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        // Cargar las librerías nativas de SQLCipher.
        SQLiteDatabase.loadLibs(context);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Lógica de migración si es necesario.
    }

    /**
     * Copia el archivo MiBase.db precifrado desde 'assets' al directorio de datos
     * de la aplicación, si el archivo aún no existe localmente.
     * @throws IOException si falla la operación de copia.
     */
    private void ensureDatabaseIsCopied() throws IOException {
        String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();
        System.out.println("DB PATH: " + dbPath);
        File dbFile = new File(dbPath);

        File dir = new File(dbFile.getParent());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!dbFile.exists()) {
            try (InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                 OutputStream outputStream = new FileOutputStream(dbPath)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                Log.d(TAG, "Copia completada.");

            } catch (IOException e) {
                throw new IOException("Fallo al copiar la base de datos precifrada.", e);
            }
        } else {
            Log.d(TAG, "DB local ya existe. Omitiendo copia.");
        }
    }

    /**
     * Abre la base de datos cifrada (SQLCipher).
     * @param password La clave de cifrado.
     * @return Instancia de SQLiteDatabase o null si falla.
     */
    public SQLiteDatabase getEncryptedWritableDatabase(String password) {
        String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();

        try {
            ensureDatabaseIsCopied(); // Copia la base de datos desde assets
        } catch (IOException e) {
            Log.e(TAG, "DB_ERROR_COPY: Falló la copia. ¿El archivo está en 'assets'?", e);
            return null;
        }

        try {
            // Cargar las librerías nativas de SQLCipher
            SQLiteDatabase.loadLibs(context);

            // Abrir la base de datos cifrada con la contraseña
            SQLiteDatabase db = SQLiteDatabase.openDatabase(
                    dbPath,
                    password,
                    null,
                    SQLiteDatabase.OPEN_READWRITE
            );

            return db;

        } catch (Exception e) {
            Log.e(TAG, "DB_ERROR_CIPHER: Clave INCORRECTA o DB corrupta/sin cifrar.", e);
            return null;
        }
    }
    // --- MÉTODOS DE UTILIDAD ---

    /**
     * Elimina el archivo de la base de datos local.
     * Útil para forzar una copia nueva (depuración).
     * @param context Contexto de la aplicación.
     * @param dbName Nombre del archivo de la DB a eliminar.
     * @return true si se eliminó, false si falló o no existía.
     */
    public static boolean deleteDatabase(Context context, String dbName) {
        try {
            boolean deleted = context.deleteDatabase(dbName);
            if (deleted) {
                Log.d(TAG, "Base de datos eliminada: " + dbName);
            } else {
                Log.w(TAG, "No se pudo eliminar la base de datos: " + dbName);
            }
            return deleted;
        } catch (Exception e) {
            Log.e(TAG, "Error al intentar eliminar la base de datos.", e);
            return false;
        }
    }
    public net.sqlcipher.database.SQLiteDatabase getWritableDatabase() {
        // ¡No usar!
        throw new RuntimeException("ERROR: Use getEncryptedWritableDatabase(password) para la DB cifrada.");
    }

    public net.sqlcipher.database.SQLiteDatabase getReadableDatabase() {
        // ¡No usar!
        throw new RuntimeException("ERROR: Use getEncryptedWritableDatabase(password) para la DB cifrada.");
    }
}


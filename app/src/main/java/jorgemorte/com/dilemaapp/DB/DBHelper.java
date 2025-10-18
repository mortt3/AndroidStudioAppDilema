package jorgemorte.com.dilemaapp.DB;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MiBase.db";
    private static final int DATABASE_VERSION = 2;
    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        SQLiteDatabase.loadLibs(context); // carga librerías SQLCipher
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No haces nada porque la base ya está creada y llena
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implementa si haces futuras actualizaciones
    }

    public SQLiteDatabase openDatabase(String password) {
        String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();
        return SQLiteDatabase.openDatabase(dbPath, password, null, SQLiteDatabase.OPEN_READWRITE);
    }
}


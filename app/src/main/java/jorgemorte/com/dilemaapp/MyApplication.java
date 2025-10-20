package jorgemorte.com.dilemaapp;

import android.app.Application;
import net.sqlcipher.database.SQLiteDatabase;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Carga las bibliotecas de SQLCipher una sola vez al iniciar la aplicación.
        SQLiteDatabase.loadLibs(this);
    }
}

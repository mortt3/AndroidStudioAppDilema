package jorgemorte.com.dilemaapp.InterfazGUI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import jorgemorte.com.dilemaapp.DB.DBHelper;
import jorgemorte.com.dilemaapp.DB.JugadorHelper;
import jorgemorte.com.dilemaapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Jugadores#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Jugadores extends Fragment {
    private DBHelper dbHelper;
    private JugadorHelper jugadorHelper;
    private static final String PASSWORD = "1234";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Jugadores() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Jugadores.
     */
    // TODO: Rename and change types and number of parameters
    public static Jugadores newInstance(String param1, String param2) {
        Jugadores fragment = new Jugadores();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jugadores, container, false);
    }

    private void cargarJugadoresDesdeDB(){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getEncryptedWritableDatabase(PASSWORD);
            if (db == null){
                Log.e("DB_GAME_LOAD", "Fallo al abrir DB");
                return;
            }
            cursor = db.rawQuery("SELECT * FROM jugador", null);


        }catch (Exception ex){
        }finally {
            if (cursor != null){
                cursor.close();
            }
            if (db != null && db.isOpen()){
                db.close();
            }
        }
    }
}
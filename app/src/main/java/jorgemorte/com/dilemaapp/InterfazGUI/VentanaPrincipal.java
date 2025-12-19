package jorgemorte.com.dilemaapp.InterfazGUI;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import jorgemorte.com.dilemaapp.R;

public class VentanaPrincipal extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_principal);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //selecionar al iniciarse el fragment de games
        bottomNavigationView.setSelectedItemId(R.id.games);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new games()).commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.players) {
                selectedFragment = new VentanaJugadores();
            }else if (id == R.id.games) {
                selectedFragment = new games();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
            return false;
        });
    }

}
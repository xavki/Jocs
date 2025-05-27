package cat.institutmarianao.jocs

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class PuntuacionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puntuacions)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val listView: ListView = findViewById(R.id.listViewPuntuacions)

        // Puntuación nueva
        val puntuacioNova = intent.getIntExtra("puntuacion", 0)

        // Nombre
        val nomDeIntent = intent.getStringExtra("nombre")
        val prefsUsuarios = getSharedPreferences("Nombres de usuario", MODE_PRIVATE)
        val nomJugador = nomDeIntent ?: prefsUsuarios.getString("nombreJugador", "Jugador")!!

        // Cargamos el map de puntuaciones
        val sharedPrefs: SharedPreferences =
            getSharedPreferences("Puntuacions", MODE_PRIVATE)
        val puntuacionsMap = sharedPrefs
            .all
            .mapValues { it.value.toString().toIntOrNull() ?: 0 }
            .toMutableMap()

        // Actualizamos si ha mejorado
        val puntuacioAnterior = puntuacionsMap[nomJugador] ?: 0
        if (puntuacioNova > puntuacioAnterior) {
            puntuacionsMap[nomJugador] = puntuacioNova
            sharedPrefs.edit()
                .putInt(nomJugador, puntuacioNova)
                .apply()
        }

        // Ordenamos y mostramos top 5
        val puntuacionsOrdenades = puntuacionsMap.entries
            .sortedByDescending { it.value }
            .take(5)
            .map { "${it.key}: ${it.value}" }

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            puntuacionsOrdenades
        )
    }
}
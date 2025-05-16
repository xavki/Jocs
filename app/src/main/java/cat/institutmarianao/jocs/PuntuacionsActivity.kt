package cat.institutmarianao.jocs

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class PuntuacionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puntuacions)

        val listView: ListView = findViewById(R.id.listViewPuntuacions)

        // 1) Puntuación nueva
        val puntuacioNova = intent.getIntExtra("puntuacion", 0)

        // 2) Nombre (Intent o SharedPreferences)
        val nomDeIntent = intent.getStringExtra("nombre")
        val prefsUsuarios = getSharedPreferences("Nombres de usuario", MODE_PRIVATE)
        val nomJugador = nomDeIntent ?: prefsUsuarios.getString("nombreJugador", "Jugador")!!

        // 3) Cargamos el map de puntuaciones
        val sharedPrefs: SharedPreferences =
            getSharedPreferences("Puntuacions", MODE_PRIVATE)
        val puntuacionsMap = sharedPrefs
            .all
            .mapValues { it.value.toString().toIntOrNull() ?: 0 }
            .toMutableMap()

        // 4) Actualizamos si ha mejorado
        val puntuacioAnterior = puntuacionsMap[nomJugador] ?: 0
        if (puntuacioNova > puntuacioAnterior) {
            puntuacionsMap[nomJugador] = puntuacioNova
            sharedPrefs.edit()
                .putInt(nomJugador, puntuacioNova)
                .apply()
        }

        // 5) Ordenamos y mostramos top 5
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

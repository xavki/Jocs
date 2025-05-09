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

        // Obtenir dades del jugador i la puntuació
        val puntuacioNova = intent.getIntExtra("puntuacion", 0)
        val nomJugador = intent.getStringExtra("nombre") ?: "Jugador Desconegut"

        val sharedPrefs: SharedPreferences = getSharedPreferences("Puntuacions", MODE_PRIVATE)
        val puntuacionsMap = sharedPrefs.all.mapValues { it.value.toString().toIntOrNull() ?: 0 }.toMutableMap()

        // Actualitzar la puntuació del jugador si és més alta que l'anterior
        val puntuacioAnterior = puntuacionsMap[nomJugador] ?: 0
        if (puntuacioNova > puntuacioAnterior) {
            puntuacionsMap[nomJugador] = puntuacioNova
            sharedPrefs.edit().putInt(nomJugador, puntuacioNova).apply()
        }

        // Ordenar per puntuació descendent i quedar-se amb les top 5
        val puntuacionsOrdenades = puntuacionsMap.entries
            .sortedByDescending { it.value }
            .take(5)
            .map { "${it.key}: ${it.value}" }

        // Mostrar les puntuacions
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, puntuacionsOrdenades)
        listView.adapter = adapter
    }
}

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

        // Obtener las puntuaciones de SharedPreferences
        val sharedPrefs: SharedPreferences = getSharedPreferences("Puntuacions", MODE_PRIVATE)
        val puntuacions = sharedPrefs.getStringSet("puntuacions", setOf())?.toList() ?: listOf()

        // Crear un adaptador para mostrar las puntuaciones
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, puntuacions)
        listView.adapter = adapter
    }

    // Función para guardar la puntuación con el nombre del jugador
    fun guardarPuntuacions(puntuacionJugador: Int, nombreJugador: String) {
        val sharedPrefs: SharedPreferences = getSharedPreferences("Puntuacions", MODE_PRIVATE)
        val puntuacionsGuardadas =
            sharedPrefs.getStringSet("puntuacions", setOf())?.toMutableList() ?: mutableListOf()

        // Formatear la puntuación con el nombre del jugador
        val puntuacionConNombre = "$nombreJugador: $puntuacionJugador"

        // Agregar la nueva puntuación
        puntuacionsGuardadas.add(puntuacionConNombre)

        // Guardar las puntuaciones actualizadas
        val editor = sharedPrefs.edit()
        editor.putStringSet("puntuacions", puntuacionsGuardadas.toSet())
        editor.apply()
    }
}

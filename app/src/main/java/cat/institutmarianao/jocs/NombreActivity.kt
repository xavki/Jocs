package cat.institutmarianao.jocs

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NombreActivity : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var guardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nombre)

        name = findViewById(R.id.inputNombreJugador)
        guardar = findViewById(R.id.buttonGuardarNombre)

        guardar.setOnClickListener {
            val nombreJugador = name.text.toString()

            // Verificar que el nombre no esté vacío
            if (nombreJugador.isNotBlank()) {
                // Guardar el nombre del jugador en SharedPreferences
                val sharedPrefs: SharedPreferences = getSharedPreferences("Nombres de usuario", MODE_PRIVATE)
                val editor = sharedPrefs.edit()

                // Obtener la lista de jugadores y sus puntuaciones guardados
                val jugadores = sharedPrefs.getString("jugadores", null)

                // Si hay jugadores previos, los recuperamos y añadimos el nuevo jugador con su puntuación
                val listaJugadores = if (jugadores == null) {
                    mutableListOf<String>()
                } else {
                    jugadores.split(",").toMutableList()
                }

                // Aquí agregamos el nuevo jugador. Asumimos que la puntuación es 0 por ahora, y la actualizaremos más tarde.
                listaJugadores.add("$nombreJugador:0")

                // Guardar la lista actualizada de jugadores y sus puntuaciones
                editor.putString("jugadores", listaJugadores.joinToString(","))
                editor.apply()

                Toast.makeText(this, "Nombre guardado: $nombreJugador", Toast.LENGTH_SHORT).show()

                // Una vez guardado el nombre, cerrar la actividad para volver a MainActivity
                finish()
            } else {
                Toast.makeText(this, "Por favor, ingresa un nombre.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

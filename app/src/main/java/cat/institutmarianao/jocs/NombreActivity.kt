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
            val nombreJugador = name.text.toString().trim()

            if (nombreJugador.isNotBlank()) {
                val sharedPrefs = getSharedPreferences("Puntuacions", MODE_PRIVATE)
                val puntuacions = sharedPrefs.all.mapValues { it.value.toString().toIntOrNull() ?: 0 }

                if (puntuacions.containsKey(nombreJugador)) {
                    Toast.makeText(this, "Aquest nom ja existeix.", Toast.LENGTH_SHORT).show()
                } else {
                    // Guardar nom amb puntuació inicial de 0
                    val editor = sharedPrefs.edit()
                    editor.putInt(nombreJugador, 0)
                    editor.apply()

                    // També guardar per accés ràpid al nom actual
                    getSharedPreferences("Nombres de usuario", MODE_PRIVATE).edit()
                        .putString("nombreJugador", nombreJugador)
                        .apply()

                    Toast.makeText(this, "Nom guardat: $nombreJugador", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Si us plau, introdueix un nom.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

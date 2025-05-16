package cat.institutmarianao.jocs

import android.content.Intent
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
                // 1) Guardar nombre
                val prefsUsuarios = getSharedPreferences("Nombres de usuario", MODE_PRIVATE)
                prefsUsuarios.edit()
                    .putString("nombreJugador", nombreJugador)
                    .apply()

                // 2) Crear entrada inicial de puntuación a 0 si no existe
                val prefsPunts = getSharedPreferences("Puntuacions", MODE_PRIVATE)
                if (!prefsPunts.contains(nombreJugador)) {
                    prefsPunts.edit()
                        .putInt(nombreJugador, 0)
                        .apply()
                }

                // 3) Lanzar la actividad de juego
                val intent = Intent(this, JocActivity::class.java)
                intent.putExtra("nombreJugador", nombreJugador)
                startActivity(intent)
                // 4) Cerrar NombreActivity
                finish()
            } else {
                Toast.makeText(this, "Si us plau, introdueix un nom.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

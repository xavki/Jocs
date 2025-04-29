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

            // Guardar el nombre del jugador en SharedPreferences
            val sharedPrefs: SharedPreferences = getSharedPreferences("Nombres de usuario", MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putString("nombreJugador", nombreJugador)
            editor.apply()


            Toast.makeText(this, "Nombre guardado: $nombreJugador", Toast.LENGTH_SHORT).show()


            finish()
        }
    }
}

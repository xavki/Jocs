package cat.institutmarianao.jocs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class JocActivity : AppCompatActivity() {
    private lateinit var vista: VistaJoc


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jocs)

        vista = findViewById(R.id.VistaJoc)

        // 1) Lee de extras u/or SharedPreferences
        val nombreExtra = intent.getStringExtra("nombreJugador")
        val prefs = getSharedPreferences("Nombres de usuario", MODE_PRIVATE)
        val nombrePrefs = prefs.getString("nombreJugador", "Jugador")!!

        // 2) Se lo asignas a la vista
        vista.nomJugador = nombreExtra ?: nombrePrefs



    }
}
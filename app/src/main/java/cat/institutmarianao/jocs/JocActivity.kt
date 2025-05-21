package cat.institutmarianao.jocs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class JocActivity : AppCompatActivity() {

    private lateinit var vista: VistaJoc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, Musicservice::class.java).also {
            it.action = "STOP"
            startService(it)
        }

        // Inflas tu layout y arrancas la vista
        setContentView(R.layout.activity_jocs)
        vista = findViewById(R.id.VistaJoc)

        val nombreExtra = intent.getStringExtra("nombreJugador")
        val prefs = getSharedPreferences("Nombres de usuario", MODE_PRIVATE)
        val nombrePrefs = prefs.getString("nombreJugador", "Jugador")!!

        // Se lo asignas a la vista
        vista.nomJugador = nombreExtra ?: nombrePrefs


        //Leer la preferencia si está activa volver a arrancar la música
        val playMusic = PreferenceManager
            .getDefaultSharedPreferences(this)
            .getBoolean("opcion1", true)

        if (playMusic) {
            Intent(this, Musicservice::class.java).also {
                it.action = "START"
                startService(it)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        //Si la preferencia sigue ON volvemos a arrancar el servicio al cerrar el juego
        val prefs = PreferenceManager
            .getDefaultSharedPreferences(this)
        if (prefs.getBoolean("opcion1", true)) {
            Intent(this, Musicservice::class.java).also {
                it.action = "START"
                startService(it)
            }
        }
    }


}
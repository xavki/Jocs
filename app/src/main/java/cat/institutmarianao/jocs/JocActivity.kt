package cat.institutmarianao.jocs

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class JocActivity : AppCompatActivity() {

    private lateinit var vista: VistaJoc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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


        val playJoc = PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean("opcion_joc", true)

        Intent(this, Musicservice::class.java).also {
            it.action = if (playJoc)
                "START_JOC"
            else
                "STOP"
            startService(it)
        }

    }

    override fun onStart() {
        super.onStart()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        Intent(this, Musicservice::class.java).also {
            it.action = if (prefs.getBoolean("opcion_joc", true)) "START_JOC" else "STOP"
            startService(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            Intent(this, Musicservice::class.java).also {
                it.action = "STOP"
                startService(it)
            }
        }
    }


}
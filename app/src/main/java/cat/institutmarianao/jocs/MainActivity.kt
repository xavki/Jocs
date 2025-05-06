package cat.institutmarianao.jocs

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity() {
    lateinit var appTitle: TextView
    lateinit var puntuacion: Button
    lateinit var sortir: Button
    lateinit var jugar: Button
    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myToolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val playMusic = sharedPreferences.getBoolean("opcion1", true) // Valor predeterminado true

        if (playMusic) {
            startMusic()
        }

        appTitle = findViewById(R.id.appTitle)
        puntuacion = findViewById(R.id.btnPuntuacions)
        sortir = findViewById(R.id.btnSortir)
        jugar = findViewById(R.id.btnJugar)


        // Aplicar l'animació
        val animacio = AnimationUtils.loadAnimation(this, R.anim.animacio_text)
        appTitle.startAnimation(animacio)


        puntuacion.setOnClickListener {
            val intent = Intent(this, PuntuacionsActivity::class.java)
            startActivity(intent)
        }

        sortir.setOnClickListener {
            finish()
        }

        jugar.setOnClickListener {
            // Obtener el nombre guardado de SharedPreferences
            val sharedPrefs = getSharedPreferences("Nombres de usuario", MODE_PRIVATE)
            val nombreJugador = sharedPrefs.getString("nombreJugador", null)

            if (nombreJugador == null) {
                // Si no hay un nombre guardado, redirigir a NombreActivity para ingresar el nombre
                val intent = Intent(this, NombreActivity::class.java)
                startActivity(intent)
            } else {
                // Si ya hay un nombre guardado, mostrar un AlertDialog preguntando si desea reemplazarlo
                val builder = AlertDialog.Builder(this)
                builder.setTitle("¿Nuevo Jugador?")
                builder.setMessage("Actualmente el nombre guardado es: $nombreJugador. ¿Quieres reemplazarlo con un nuevo nombre?")

                builder.setPositiveButton("Sí") { _, _ ->
                    // Si el jugador decide reemplazarlo, redirigir a NombreActivity para que ingrese un nuevo nombre
                    val intent = Intent(this, NombreActivity::class.java)
                    startActivity(intent)
                }

                builder.setNegativeButton("No") { _, _ ->
                    // Si el jugador no quiere reemplazarlo, solo iniciar VistaJoc con el nombre guardado
                    Toast.makeText(this, "Bienvenido de nuevo, $nombreJugador", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, JocActivity::class.java)
                    startActivity(intent)
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }
        }




    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_configuracio) {
            val intent = Intent(this, ConfiguracioActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.menu_informacio) {
            mostrarInformacio()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun mostrarInformacio() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Informació de l'aplicació")
        builder.setMessage(
            "Nom de l'app: Ninja Warrior\n" + "Versió: 1.0\n" + "Desenvolupador: Xavi martinez"
        )
        builder.setPositiveButton("Tancar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    /*   private fun mostrarDialogNomJugador() {
           val builder = AlertDialog.Builder(this)
           builder.setTitle("Nom del jugador")
           builder.setMessage("Introdueix el teu nom:")

           val input = EditText(this)
           input.hint = "Escriu aquí el teu nom"
           builder.setView(input)

           builder.setPositiveButton("OK") { dialog, _ ->
               val nom = input.text.toString()
               // Guarda el nom a SharedPreferences
               val prefs = getSharedPreferences("Noms_de_usuari", MODE_PRIVATE)
               prefs.edit().putString("nomJugador", nom).apply()
               Toast.makeText(this, "Nom guardat: $nom", Toast.LENGTH_SHORT).show()
           }

           builder.setNegativeButton("Cancel·lar") { dialog, _ ->
               dialog.dismiss()
           }

           builder.show()
       }*/
    // Iniciar música si no está sonando
    private fun startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.pokemontcg)
            mediaPlayer?.isLooping = true
        }

        if (!mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
        }
    }


    override fun onStart() {
        super.onStart()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (prefs.getBoolean("opcion1", true)) {
            val intent = Intent(this, Musicservice::class.java)
            intent.action = "START"
            startService(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        // No se detiene el servicio aquí para que la música siga
    }

    override fun onDestroy() {
        super.onDestroy()
        // No liberamos mediaPlayer aquí porque lo gestiona el servicio
    }
}



package cat.institutmarianao.jocs

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity() {
    lateinit var appTitle: TextView
    lateinit var puntuacion: Button
    lateinit var sortir: Button
    lateinit var jugar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myToolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val playMusic = sharedPreferences.getBoolean("opcion1", true) // Valor predeterminado true

        if (playMusic) {
            Intent(this, Musicservice::class.java).also {
                it.action = "START"
                startService(it)
            }
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
            Intent(this, Musicservice::class.java).also {
                it.action = "STOP"
                startService(it)
            }
            finish()
        }

        jugar.setOnClickListener {
            val intent = Intent(this, NombreActivity::class.java)
            startActivity(intent)
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
        val builder = AlertDialog.Builder(this)
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


    override fun onStart() {
        super.onStart()
        // Arrancamos siempre el Musicservice si “Reproducir música” está activo
        val prefs = PreferenceManager
            .getDefaultSharedPreferences(this)
        if (prefs.getBoolean("opcion1", true)) {
            Intent(this, Musicservice::class.java).also {
                it.action = "START"
                startService(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cuando la Activity se cierra definitivamente, enviamos STOP para pausar la música
        if (isFinishing) {
            Intent(this, Musicservice::class.java).also {
                it.action = "STOP"
                startService(it)
            }
        }
    }

}



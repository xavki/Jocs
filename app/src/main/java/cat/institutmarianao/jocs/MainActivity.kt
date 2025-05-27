package cat.institutmarianao.jocs

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import kotlinx.coroutines.NonCancellable.start


class MainActivity : AppCompatActivity() {
    lateinit var puntuacion: Button
    lateinit var sortir: Button
    lateinit var jugar: Button

    private var musicaInici: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val myToolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        Intent(this, Musicservice::class.java).also {
            it.action = if (sharedPreferences.getBoolean("opcion1", true))
                "START_INICIO"
            else
                "STOP"
            startService(it)
        }


        puntuacion = findViewById(R.id.btnPuntuacions)
        sortir = findViewById(R.id.btnSortir)
        jugar = findViewById(R.id.btnJugar)


        val ninjaView = findViewById<ImageView>(R.id.appTitle)
        ninjaView.post {
            val parentW = (ninjaView.parent as View).width.toFloat()
            val parentH = (ninjaView.parent as View).height.toFloat()
            val w = ninjaView.width.toFloat()
            val h = ninjaView.height.toFloat()

            // Cantonada inferior esquerra
            ninjaView.translationX = 0f
            ninjaView.translationY = parentH - h

            val topRightX = parentW - w
            val topRightY = 0f
            val centerX = (parentW - w) / 2
            val centerY = (parentH - h) / 2

            val animMoveOut = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(ninjaView, "translationX", 0f, topRightX),
                    ObjectAnimator.ofFloat(ninjaView, "translationY", parentH - h, topRightY)
                )
                duration = 2000
            }

            val animZoom = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(ninjaView, "scaleX", 1f, 1.5f),
                    ObjectAnimator.ofFloat(ninjaView, "scaleY", 1f, 1.5f)
                )
                duration = 2000
            }

            val animMoveCenter = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(ninjaView, "translationX", topRightX, centerX),
                    ObjectAnimator.ofFloat(ninjaView, "translationY", topRightY, centerY)
                )
                duration = 2000
            }

            val animRotate = ObjectAnimator.ofFloat(ninjaView, "rotation", 0f, 360f).apply {
                duration = 2000
            }

            val fullAnim = AnimatorSet().apply {
                playSequentially(animMoveOut, animZoom, animMoveCenter, animRotate)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        ninjaView.scaleX = 1f
                        ninjaView.scaleY = 1f
                        ninjaView.rotation = 0f
                        start()  // bucle
                    }
                })
            }
            fullAnim.start()
        }


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
            musicaInici?.stop()
            musicaInici?.release()
            musicaInici = null

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

    override fun onStart() {
        super.onStart()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        Intent(this, Musicservice::class.java).also {
            it.action = if (sharedPreferences.getBoolean("opcion1", true))
                "START_INICIO"
            else
                "STOP"
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



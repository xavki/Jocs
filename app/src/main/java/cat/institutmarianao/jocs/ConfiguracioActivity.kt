package cat.institutmarianao.jocs

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class ConfiguracioActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracio)

        // Cargar el fragmento de preferencias
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ConfiguracioFragment())
                .commit()

            // Reemplazamos el fragmento de configuración con el ImagesFragment
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(
                R.id.fragment_container,
                Imagesfragments()
            )
            fragmentTransaction.commit()
        }
    }

    // Fragmento que gestionará las preferencias
    class ConfiguracioFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            // Cargar las preferencias desde el archivo XML
            setPreferencesFromResource(R.xml.preferences, rootKey)

            // Obtenemos la preferencia "Reproducir música"
            val musicaPref = findPreference<CheckBoxPreference>("opcion1")
            musicaPref?.setOnPreferenceChangeListener { _, newValue ->
                val playMusic = newValue as Boolean

                // Lógica para reproducir o pausar la música
                if (playMusic) {
                    (activity as ConfiguracioActivity).startMusic()  // Inicia la música si no está sonando
                } else {
                    (activity as ConfiguracioActivity).pauseMusic()  // Pausa la música si está sonando
                }

                true
            }

            // Mostrar preferencias y leer los valores en cualquier momento si es necesario
            val sharedPreferences =
                context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val playMusic = sharedPreferences?.getBoolean("opcion1", true) ?: true

            // Si la música está activada desde las preferencias, la iniciamos
            if (playMusic) {
                (activity as ConfiguracioActivity).startMusic()
            }
        }
    }

    // Iniciar música si no está sonando
    fun startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.pokemontcg)
            mediaPlayer?.isLooping = true
        }

        if (!mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
        }
    }

    // Pausar la música
    fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    // Liberar recursos
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

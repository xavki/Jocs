package cat.institutmarianao.jocs

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import android.widget.Toast
import androidx.preference.CheckBoxPreference

class ConfiguracioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecer el contenido de la actividad directamente desde el archivo de preferencias
        setContentView(R.layout.activity_configuracio)

        // Cargar las preferencias usando PreferenceFragmentCompat
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ConfiguracioFragment())
                .commit()
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
                    if (MainActivity.mediaPlayer == null) {
                        MainActivity.mediaPlayer = MediaPlayer.create(requireContext(), R.raw.pokemontcg)
                        MainActivity.mediaPlayer?.isLooping = true
                    }
                    MainActivity.mediaPlayer?.start()
                } else {
                    MainActivity.mediaPlayer?.pause()
                }

                true
            }

            // Mostrar preferencias y leer los valores en cualquier momento si es necesario
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val opcion2Value = sharedPreferences?.getString("opcion2", "5")
            val opcion3Value = sharedPreferences?.getString("opcion3", "1")

        }
    }
}

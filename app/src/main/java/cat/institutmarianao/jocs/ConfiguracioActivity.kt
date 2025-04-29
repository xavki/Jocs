package cat.institutmarianao.jocs

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
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

            // Obtener el número de enemigos desde la preferencia "opcion3"
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val numEnemigos = sharedPreferences?.getString("opcion3", "1")?.toIntOrNull() ?: 1


            actualizarNumeroDeEnemigos(numEnemigos)
        }

        // Método para actualizar el número de enemigos en el juego
        private fun actualizarNumeroDeEnemigos(numEnemigos: Int) {
            // Aquí puedes usar este valor para inicializar el número de enemigos en tu juego
            // Por ejemplo:
            MainActivity.numObjectius = numEnemigos
            // O cualquier otra lógica que necesites para manejar los enemigos
        }
    }
}

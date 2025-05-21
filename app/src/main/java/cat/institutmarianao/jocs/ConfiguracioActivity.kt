package cat.institutmarianao.jocs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class ConfiguracioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracio)

        if (savedInstanceState == null) {
            // Cargamos el fragmento de configuración
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ConfiguracioFragment())
                .commit()
        }
    }

    class ConfiguracioFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            val musicaPref = findPreference<CheckBoxPreference>("opcion1")

            musicaPref?.setOnPreferenceChangeListener { _, newValue ->
                val playMusic = newValue as Boolean
                val context = activity ?: return@setOnPreferenceChangeListener false
                val intent = Intent(context, Musicservice::class.java)

                if (playMusic) {
                    intent.action = "START"
                    context.startService(intent)
                } else {
                    intent.action = "STOP"
                    context.startService(intent)
                }

                true
            }

            // Iniciar o detener música al cargar el fragmento según la preferencia actual
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val playMusic = sharedPrefs.getBoolean("opcion1", true)
            val intent = Intent(requireContext(), Musicservice::class.java)
            intent.action = if (playMusic) "START" else "STOP"
            requireContext().startService(intent)


            // Obtener la preferencia de "Número de enemigos"
            val numObjectiusPref = findPreference<EditTextPreference>("opcion3")
            numObjectiusPref?.setOnPreferenceChangeListener { _, newValue ->
                val newNumObjectius = newValue.toString().toIntOrNull() ?: 1  // Convertir a número

                // Verifica si el valor se guarda correctamente
                Log.d("ConfiguracioFragment", "Nuevo número de enemigos: $newNumObjectius")

                // Guardar el nuevo número de enemigos en las preferencias
                val sharedPrefs = context?.getSharedPreferences("Nombres de usuario", Context.MODE_PRIVATE)
                if (sharedPrefs != null) {
                    sharedPrefs.edit().putInt("opcion3", newNumObjectius).apply()
                }
                Log.d("ConfiguracioFragment", "Nuevo número de enemigos guardado: $newNumObjectius")



                true

            }

        }
    }
}

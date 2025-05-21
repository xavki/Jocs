package cat.institutmarianao.jocs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class ConfiguracioActivity : AppCompatActivity() {

    companion object {
        private const val MAX_OBJETIUS = 200  // el límite que quieras
    }
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

            // Asegurarnos de que solo se puedan teclear dígitos
            numObjectiusPref?.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }

            numObjectiusPref?.setOnPreferenceChangeListener { _, newValue ->
                // 1) Parseamos a Int o null
                val newNumObjectius = newValue.toString().toIntOrNull()

                // 2) Validamos el rango 1..MAX_OBJETIUS
                if (newNumObjectius == null || newNumObjectius !in 1..MAX_OBJETIUS) {
                    Toast
                        .makeText(
                            context,
                            "Introduce un número entre 1 y $MAX_OBJETIUS",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    return@setOnPreferenceChangeListener false
                }

                // 3) (Opcional) actualizar sumario para que se vea el valor
                numObjectiusPref.summary = newNumObjectius.toString()

                // 4) Guardar en SharedPreferences si necesitas un archivo distinto
                val sharedPrefs = context
                    ?.getSharedPreferences("Nombres de usuario", Context.MODE_PRIVATE)
                sharedPrefs
                    ?.edit()
                    ?.putInt("opcion3", newNumObjectius)
                    ?.apply()

                Log.d("ConfiguracioFragment", "Número válido de enemigos: $newNumObjectius")
                true  // devolvemos true para que se guarde
            }

        }
    }
}

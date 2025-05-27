package cat.institutmarianao.jocs

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
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
        private const val MAX_OBJETIUS = 200  // el límite que quieras de enemigos
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracio)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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

            val opc1 = findPreference<CheckBoxPreference>("opcion1")
            opc1?.setOnPreferenceChangeListener { _, newValue ->
                val enabled = newValue as Boolean
                Intent(requireContext(), Musicservice::class.java).also {
                    it.action = if (enabled) "START_INICIO" else "STOP"
                    requireContext().startService(it)
                }
                true
            }



            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
            Intent(requireContext(), Musicservice::class.java).also {
                it.action = if (prefs.getBoolean("opcion1", true)) "START_INICIO" else "STOP"
                requireContext().startService(it)
            }

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
                val newNumObjectius = newValue.toString().toIntOrNull()

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

                numObjectiusPref.summary = newNumObjectius.toString()

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

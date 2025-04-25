package cat.institutmarianao.jocs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
class Imagesfragments : PreferenceFragmentCompat() {

    private lateinit var ninjaPref: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        ninjaPref = findPreference("opcion2")!!

        ninjaPref.setOnPreferenceClickListener {
            showNinjaDialog()
            true
        }
    }
    private fun showNinjaDialog() {
        Log.d("Dialog", "showNinjaDialog called")  // Esto nos ayudará a saber si entra al método

        val ninjaNames = arrayOf("Ninja1", "Ninja2", "Ninja3")
        val ninjaValues = arrayOf("1", "2", "3")
        val ninjaImages = arrayOf(
            R.drawable.ninja01,
            R.drawable.ninja02,
            R.drawable.ninja03
        )


        val builder = AlertDialog.Builder(requireContext())
        val adapter = object : ArrayAdapter<String>(requireContext(), R.layout.item_ninja_image, ninjaNames) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_ninja_image, parent, false)

                Log.d("Image", "Setting image for ninja: ${ninjaNames[position]} with image resource: ${ninjaImages[position]}")

                val imageView = view.findViewById<ImageView>(R.id.ninja_icon)
                val textView = view.findViewById<TextView>(R.id.ninja_name)

                imageView.setImageResource(ninjaImages[position])
                textView.text = ninjaNames[position]

                return view
            }

        }

        builder.setTitle("Selecciona un ninja")
            .setAdapter(adapter) { dialog, which ->
                val prefs = preferenceManager.sharedPreferences
                prefs?.edit()?.putString("opcion2", ninjaValues[which])?.apply()
                ninjaPref.summary = ninjaNames[which]
            }
            .show()

    }



}

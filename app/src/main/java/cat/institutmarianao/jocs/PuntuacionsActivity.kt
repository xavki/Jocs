package cat.institutmarianao.jocs

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PuntuacionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puntuacions)

        val listView: ListView = findViewById(R.id.listViewPuntuacions)

        // Obtener las puntuaciones de SharedPreferences
        val sharedPrefs: SharedPreferences = getSharedPreferences("Puntuacions", MODE_PRIVATE)
        val puntuacions = sharedPrefs.getStringSet("puntuacions", setOf())?.toList() ?: listOf()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, puntuacions)
        listView.adapter = adapter
    }

        private fun mostrarPuntuacionsDialog() {
            val puntuacions = listOf("120 Marga", "80 Richard", "20 Juanjo")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Game Score")

            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(50, 30, 50, 30)
                background = ContextCompat.getDrawable(context, R.drawable.degradat)
            }

            val listView = ListView(this).apply {
                adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, puntuacions)
            }

            layout.addView(listView)
            builder.setView(layout)

            builder.setNegativeButton("Tancar") { dialog, _ -> dialog.dismiss() }

            builder.show()
        }
    }


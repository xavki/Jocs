package cat.institutmarianao.jocs

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat

class VistaJoc(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val objectius = mutableListOf<Grafics>()
    private val numObjectius = 5 // Puedes ajustar este valor según lo necesites

    init {
        val drawableEnemic: Drawable? =
            ResourcesCompat.getDrawable(resources, R.drawable.ninja_enemic, null)

        drawableEnemic?.let {
            val objectiu = Grafics(this, it)
            objectiu.incY = Math.random() * 4 - 2
            objectiu.incX = Math.random() * 4 - 2
            objectiu.rotacio = (Math.random() * 8 - 4).toInt()
            objectius.add(objectiu)
        }
    }

    override fun onSizeChanged(ancho: Int, alto: Int, anchoAnt: Int, altoAnt: Int) {
        super.onSizeChanged(ancho, alto, anchoAnt, altoAnt)
        // Un cop tenim les dimensions, posicionem els objectius
        for (objectiu in objectius) {
            objectiu.posX = Math.random() * (ancho - objectiu.amplada)
            objectiu.posY = Math.random() * (alto - objectiu.altura)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (objectiu in objectius) {
            objectiu.dibuixaGrafic(canvas)
        }
    }

    // Suponiendo que esta función se llama cuando termina el juego:
    fun finalizarJuego(puntuacionJugador: Int) {
        // Obtener el nombre del jugador
        val sharedPrefs: SharedPreferences =
            context.getSharedPreferences("Nombres de usuario", Context.MODE_PRIVATE)
        val nombreJugador = sharedPrefs.getString("nombreJugador", "Jugador Desconocido")

        // Guardar la puntuación con el nombre del jugador en PuntuacionsActivity
        val puntuacionesActivity = PuntuacionsActivity()
        puntuacionesActivity.guardarPuntuacions(
            puntuacionJugador,
            nombreJugador ?: "Jugador Desconocido"
        )

        // Luego se puede redirigir a la pantalla de puntuaciones
        val intent = Intent(context, PuntuacionsActivity::class.java)
        context.startActivity(intent)
    }
}

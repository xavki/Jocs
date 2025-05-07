package cat.institutmarianao.jocs

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat

class VistaJoc(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val objectius = mutableListOf<Grafics>()
    private val numObjectius: Int  // Este será el número de enemigos que leeremos de las preferencias
    private lateinit var drawableNinja: Drawable

    // //// NINJA //////
    // //// NINJA //////
    private var ninja: Grafics? = null  // Gràfic del ninja
    private val girNinja = 0           // Increment de direcció
    private val acceleracioNinja = 0f  // augment de velocitat

    // Increment estàndard de gir i acceleració
    private val INC_GIR = 5
    private val INC_ACCELERACIO = 0.5f



    init {
        // Recuperamos el número de enemigos desde las preferencias
        val sharedPrefs: SharedPreferences =
            context.getSharedPreferences("Nombres de usuario", Context.MODE_PRIVATE)
        numObjectius = sharedPrefs.getInt("opcion3", 1)  // 'opcion3' es la clave de la preferencia
        Log.d("VistaJoc", "Número de enemigos: $numObjectius")

        // Crear los enemigos de acuerdo al número de enemigos (numObjectius)
        for (i in 0 until numObjectius) {
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

        drawableNinja = ResourcesCompat.getDrawable(resources, R.drawable.ninja01, null)!!
        ninja = Grafics(this, drawableNinja)

        /*if (condicion) {
            drawableNinja = ResourcesCompat.getDrawable(resources, R.drawable.ninja01, null)
        } else if (otraCondicion) {
            drawableNinja = ResourcesCompat.getDrawable(resources, R.drawable.ninja02, null)
        } else {
            drawableNinja = ResourcesCompat.getDrawable(resources, R.drawable.ninja03, null)
        }*/


    }

    override fun onSizeChanged(ancho: Int, alto: Int, anchoAnt: Int, altoAnt: Int) {
        super.onSizeChanged(ancho, alto, anchoAnt, altoAnt)

        // Verificar si ninja está inicializado correctamente
        // Comprobamos que ninja no es null
        ninja?.let {
            // Calculamos el centro de la vista
            val centroX = (ancho - it.amplada) / 2  // Centrado horizontal
            val centroY = (alto - it.altura) / 2    // Centrado vertical

            // Actualizamos las posiciones de ninja
            it.posX = centroX.toDouble()
            it.posY = centroY.toDouble()
        }

        // Posicionamos los objectius aleatoriamente, asegurándonos de que estén lejos del ninja
        for (objectiu in objectius) {
            do {
                // Posicionamos el objectiu aleatoriamente dentro de la vista
                objectiu.posX = Math.random() * (ancho - objectiu.amplada)
                objectiu.posY = Math.random() * (alto - objectiu.altura)
            } while (objectiu.distancia(ninja!!) < (ancho + alto) / 5)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Dibujar el enemigos
        for (objectiu in objectius) {
            objectiu.dibuixaGrafic(canvas)
        }
        // Dibujar el ninja
        ninja?.let {
            it.dibuixaGrafic(canvas)
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

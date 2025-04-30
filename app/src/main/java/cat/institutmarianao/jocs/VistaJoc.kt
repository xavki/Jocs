package cat.institutmarianao.jocs

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class VistaJoc(context: Context, attrs: AttributeSet) : View(context, attrs) {

   /* private var objectius: MutableList<Grafics> = mutableListOf()

    init {
        val drawableEnemic: Drawable = context.resources.getDrawable(R.drawable.ninja_enemic, null)

        // Inicialización de los objetos
        for (i in 0 until numObjectius) {
            val objectiu = Grafics(this, drawableEnemic)
            objectiu.setIncY(Random.nextDouble() * 4 - 2)
            objectiu.setIncX(Random.nextDouble() * 4 - 2)
            objectiu.setAngle(Random.nextInt(360).toInt())
            objectiu.setRotacio(Random.nextInt(8) - 4)
            objectius.add(objectiu)
        }
    }

    // Método que nos da el ancho y alto de la pantalla
    override fun onSizeChanged(ancho: Int, alto: Int, anchoAnterior: Int, altoAnterior: Int) {
        super.onSizeChanged(ancho, alto, anchoAnterior, altoAnterior)

        // Una vez que conocemos el ancho y alto de la pantalla, situamos los objetivos aleatoriamente
        for (objectiu in objectius) {
            objectiu.setPosX(Random.nextDouble() * (ancho - objectiu.getAmplada()))
            objectiu.setPosY(Random.nextDouble() * (alto - objectiu.getAltura()))
        }
    }

    // Método que dibuja la vista
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (objectiu in objectius) {
            objectiu.dibuixaGrafic(canvas)
        }
    }*/
}

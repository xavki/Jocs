package cat.institutmarianao.jocs

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat


@SuppressLint("UseCompatLoadingForDrawables")
class VistaJoc(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var objectius = mutableListOf<Grafics>()
    var numObjectius: Int  // Este será el número de enemigos que leeremos de las preferencias
    var drawableEnemic: Drawable
    var drawableNinja: Drawable
    var drawableGanivet: Drawable
    var puntuacionJugador: Int = 0  // Esta variable almacena la puntuación del jugador

    // //// NINJA //////
    var ninja: Grafics // Gràfic del ninja
    var girNinja = 0           // Increment de direcció
    var acceleracioNinja = 0f  // augment de velocitat

    // Increment estàndard de gir i acceleració
    var INC_GIR = 5
    var INC_ACCELERACIO = 0.5f

    // //// THREAD I TEMPS //////
    // Thread encarregat de processar el joc
    var thread: ThreadJoc = ThreadJoc()

    //Cada quant temps volem processar canvis (ms)
    var PERIODE_PROCES = 50

    // Quan es va realitzar l'últim procés
    var ultimProces: Long = 0

    var mX = 0f
    var mY: kotlin.Float = 0f
    var llancament = false

    // //// LLANÇAMENT //////
    var ganivet: Grafics
    var INC_VELOCITAT_GANIVET = 12
    var ganivetActiu = false
    var tempsGanivet = 0

    var drawableObjectiu: Array<Drawable?>
    var mpLlancament: MediaPlayer? = null
    var mpExplosio: MediaPlayer? = null

    lateinit var pare: Activity
    var nomJugador: String = "Jugador"


    init {
        // Recuperamos el número de enemigos desde las preferencias
        val sharedPrefs: SharedPreferences =
            context.getSharedPreferences("Nombres de usuario", Context.MODE_PRIVATE)
        numObjectius = sharedPrefs.getInt("opcion3", 1)  // 'opcion3' es la clave de la preferencia
        Log.d("VistaJoc", "Número de enemigos: $numObjectius")

        // Crear los enemigos de acuerdo al número de enemigos (numObjectius)
        mpLlancament = MediaPlayer.create(context, R.raw.llancament);
        mpExplosio = MediaPlayer.create(context, R.raw.explosio);
        drawableEnemic = ResourcesCompat.getDrawable(resources, R.drawable.ninja_enemic, null)!!
        drawableObjectiu = arrayOfNulls<Drawable>(numObjectius)

        if (context is Activity) {
            pare = context
        }
        this.nomJugador = nomJugador

        var nomJugador: String = "Jugador"



        for (i in 0 until numObjectius) {
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

        drawableGanivet = ResourcesCompat.getDrawable(resources, R.drawable.ganivet, null)!!
        ganivet = Grafics(this, drawableGanivet)

        /*if (condicion) {
            drawableNinja = ResourcesCompat.getDrawable(resources, R.drawable.ninja01, null)
        } else if (otraCondicion) {
            drawableNinja = ResourcesCompat.getDrawable(resources, R.drawable.ninja02, null)
        } else {
            drawableNinja = ResourcesCompat.getDrawable(resources, R.drawable.ninja03, null)
        }*/

        drawableObjectiu[0] = context.getResources().getDrawable(R.drawable.cap_ninja, null); //cap
        drawableObjectiu[1] = context.getResources().getDrawable(R.drawable.cos_ninja, null); //cos
        drawableObjectiu[2] = context.getResources().getDrawable(R.drawable.cua_ninja, null);
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
            } while (objectiu.distancia(ninja) < (ancho + alto) / 5)
        }
        ultimProces = System.currentTimeMillis()
        thread.start()

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Dibujar el enemigos
        for (objectiu in objectius) {
            objectiu.dibuixaGrafic(canvas)
        }
        // Dibujar el ninja
        ninja?.dibuixaGrafic(canvas)

        // Dibujar el ganivet
        if (ganivetActiu) {
            ganivet.dibuixaGrafic(canvas)
        }
    }

    // Suponiendo que esta función se llama cuando termina el juego:
    fun finalitzarJoc(victoria: Boolean) {
        val prefs = context.getSharedPreferences("Puntuacions", Context.MODE_PRIVATE)
        val nombre = nomJugador
        val puntuacionFinal = puntuacionJugador
        val anterior = prefs.getInt(nombre, 0)
        if (puntuacionFinal > anterior) {
            prefs.edit()
                .putInt(nombre, puntuacionFinal)
                .apply()
        }
        // 2) Volver al MainActivity
        val intent = Intent(context, MainActivity::class.java).apply {
            // Limpia la pila para no acumular actividades
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        context.startActivity(intent)
        // 3) Terminar el juego
        if (context is Activity) {
            (context as Activity).finish()
        }

    }


    inner class ThreadJoc : Thread() {
        override fun run() {
            while (true) {
                actualitzaMoviment()
            }
        }
    }

    @Synchronized
    protected fun actualitzaMoviment() {
        val instant_actual = System.currentTimeMillis()
        // No facis res si el període de procés no s'ha complert.
        if (ultimProces + PERIODE_PROCES > instant_actual) {
            return
        }
        // Per una execució en temps real calculem retard
        val retard = ((instant_actual - ultimProces) / PERIODE_PROCES).toDouble()
        ultimProces = instant_actual // Per a la propera vegada

        // Actualitzem velocitat i direcció del personatge Ninja a partir de
        // girNinja i acceleracioNinja (segons l'entrada del jugador)
        ninja.angle = (ninja.angle + girNinja * retard).toInt()

        val nIncX = ninja.incX + acceleracioNinja *
                Math.cos(Math.toRadians(ninja.angle.toDouble())) * retard

        val nIncY = ninja.incY + acceleracioNinja *
                Math.sin(Math.toRadians(ninja.angle.toDouble())) * retard

        // Actualitzem si el módul de la velocitat no és més gran que el màxim
        if (Math.hypot(nIncX, nIncY) <= Grafics.MAX_VELOCITAT) {
            ninja.incX = nIncX
            ninja.incY = nIncY
        }

        // Actualitzem posicions X i Y
        ninja.incrementaPos(retard)
        for (objectiu in objectius) {
            objectiu.incrementaPos(retard)
        }
        // Actualitzem posicions dels objectius (enemics)
        for (objectiu in objectius) {
            objectiu.incrementaPos(retard)

            // Comprovem si el ninja col·lideix amb l'objectiu (enemics)
            if (ninja.verificaColisio(objectiu)) {
                // Si hi ha col·lisió, finalitza el joc
                if (objectius.isEmpty()) {
                    finalitzarJoc(true)
                    return
                }
                for (objectiu in objectius) {
                    objectiu.incrementaPos(retard)

                    if (ninja.verificaColisio(objectiu)) {
                        finalitzarJoc(false) // derrota
                        return
                    }
                }
            }
        }

        invalidate()

        // Actualitzem posició de ganivet
        if (ganivetActiu) {
            ganivet.incrementaPos(retard)
            tempsGanivet -= retard.toInt()
            if (tempsGanivet < 0) {
                ganivetActiu = false
            } else {
                for (i in objectius.indices)
                    if (ganivet.verificaColisio(objectius.elementAt(i))) {
                        destrueixObjectiu(i)
                        break
                    }
            }
        }
    }

    private fun destrueixObjectiu(i: Int) {
        val numParts = 3
        puntuacionJugador += 10
        if (objectius[i].drawable === drawableEnemic) {
            for (n in 0 until numParts) {
                val objectiu = Grafics(this, drawableObjectiu[n]!!)
                objectiu.posX = objectius[i].posX
                objectiu.posY = objectius[i].posY
                objectiu.incX = Math.random() * 7 - 3
                objectiu.incY = Math.random() * 7 - 3
                objectiu.angle = (Math.random() * 360).toInt()
                objectiu.rotacio = (Math.random() * 8 - 4).toInt()
                objectius.add(objectiu)
            }
        }

        objectius.removeAt(i)
        ganivetActiu = false
        mpExplosio?.start();
    }

    private fun DisparaGanivet() {
        ganivet.posX = ninja.posX + ninja.amplada / 2 - ganivet.amplada / 2
        ganivet.posY = ninja.posY + ninja.altura / 2 - ganivet.altura / 2
        ganivet.angle = ninja.angle
        ganivet.incX = Math.cos(Math.toRadians(ganivet.angle.toDouble())) *
                INC_VELOCITAT_GANIVET
        ganivet.incY = Math.sin(Math.toRadians(ganivet.angle.toDouble())) *
                INC_VELOCITAT_GANIVET
        tempsGanivet = Math.min(
            this.width / Math.abs(ganivet.incX),
            this.height / Math.abs(ganivet.incY)
        ).toInt() - 2
        ganivetActiu = true
        mpLlancament?.start();
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> llancament = true
            MotionEvent.ACTION_MOVE -> {
                val dx = Math.abs(x - mX)
                val dy: Float = Math.abs(y - mY)
                if (dy < 6 && dx > 6) {
                    girNinja = Math.round((x - mX) / 2)
                    llancament = false
                } else if (dx < 6 && dy > 6) {
                    acceleracioNinja = Math.round((mY - y) / 25).toFloat()
                    llancament = false
                }
            }

            MotionEvent.ACTION_UP -> {
                girNinja = 0
                acceleracioNinja = 0f
                if (llancament) {
                    DisparaGanivet()
                }
            }
        }
        mX = x
        mY = y
        return true
    }

    override fun onKeyDown(codiTecla: Int, event: KeyEvent?): Boolean {
        super.onKeyDown(codiTecla, event)
        // Suposem que processarem la pulsació
        var procesada = true
        when (codiTecla) {
            KeyEvent.KEYCODE_DPAD_UP -> acceleracioNinja = +INC_ACCELERACIO
            KeyEvent.KEYCODE_DPAD_DOWN -> acceleracioNinja = -INC_ACCELERACIO
            KeyEvent.KEYCODE_DPAD_LEFT -> girNinja = -INC_GIR
            KeyEvent.KEYCODE_DPAD_RIGHT -> girNinja = +INC_GIR
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {}
            else ->  // Si estem aquí, no hi ha pulsació que ens interessi
                procesada = false
        }
        return procesada
    }

    override fun onKeyUp(codigoTecla: Int, evento: KeyEvent?): Boolean {
        super.onKeyUp(codigoTecla, evento)
        // Suposem que processarem la pulsació
        var procesada = true
        when (codigoTecla) {
            KeyEvent.KEYCODE_DPAD_UP -> acceleracioNinja = 0f
            KeyEvent.KEYCODE_DPAD_DOWN -> acceleracioNinja = 0f
            KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT -> girNinja = 0
            else ->  // Si estem aquí, no hi ha pulsació que ens interessi
                procesada = false
        }
        return procesada
    }

}

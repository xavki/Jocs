package cat.institutmarianao.jocs

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.annotation.RawRes

class Musicservice : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var currentType: String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START_INICIO" -> playMusic(R.raw.pokemontcg, "INICIO")
            "START_JOC"    -> playMusic(R.raw.magodeoz, "JOC")
            "STOP"         -> mediaPlayer?.pause()
        }
        return START_STICKY
    }

    private fun playMusic(@RawRes resId: Int, type: String) {
        if (currentType != type) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, resId).apply {
                isLooping = true
                start()
            }
            currentType = type
        } else if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null
}

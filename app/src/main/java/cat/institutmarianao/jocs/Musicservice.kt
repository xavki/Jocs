package cat.institutmarianao.jocs

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class Musicservice : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared = false

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.pokemontcg)
        mediaPlayer?.isLooping = true
        isPrepared = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START" -> {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.pokemontcg)
                    mediaPlayer?.isLooping = true
                    isPrepared = true
                }
                if (isPrepared && mediaPlayer?.isPlaying == false) {
                    mediaPlayer?.start()
                }
            }
            "STOP" -> {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPrepared = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

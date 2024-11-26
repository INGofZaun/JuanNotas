package com.ad_coding.noteappcourse.ui.media

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ad_coding.noteappcourse.R

class AudioPlayerActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val audioPath = intent.getStringExtra("AUDIO_PATH")

        audioPath?.let {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(it)
                prepare()
                start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}

package com.ad_coding.noteappcourse.ui.media

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.ad_coding.noteappcourse.R

class VideoPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoPath = intent.getStringExtra("VIDEO_PATH")

        videoPath?.let {
            val uri = Uri.parse(it)
            videoView.setVideoURI(uri)
            val mediaController = MediaController(this)
            videoView.setMediaController(mediaController)
            mediaController.setAnchorView(videoView)
            videoView.start()
        }
    }
}

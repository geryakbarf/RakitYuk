package xyz.geryakbarf.rakityuk.ui

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_youtube_player.*
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.utils.Server


class YoutubePlayerActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)
        val kode = intent.getStringExtra("id")

        ytPlayer.initialize(Server.api, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(
                    applicationContext,
                    "Terjadi Kesalahan, Periksa Koneksi Anda!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1!!.cueVideo(kode)
            }
        })
    }
}

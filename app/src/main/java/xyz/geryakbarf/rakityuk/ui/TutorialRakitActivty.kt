package xyz.geryakbarf.rakityuk.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tutorial_rakit_activty.*
import xyz.geryakbarf.rakityuk.R

class TutorialRakitActivty : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_rakit_activty)
        supportActionBar?.hide()
        btnVideo1.setOnClickListener(this)
        btnVideo2.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnVideo1 -> {
                val intent = Intent(applicationContext, YoutubePlayerActivity::class.java)
                intent.putExtra("id", "wffqVCOv2SU")
                startActivity(intent)
            }
            R.id.btnVideo2 -> {
                val intent = Intent(applicationContext, YoutubePlayerActivity::class.java)
                intent.putExtra("id", "ZRpAokL8CDc")
                startActivity(intent)
            }
        }
    }
}

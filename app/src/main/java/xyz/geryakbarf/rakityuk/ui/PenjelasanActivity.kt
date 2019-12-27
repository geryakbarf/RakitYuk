package xyz.geryakbarf.rakityuk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.ui.fragment.PenjelasanFragment

class PenjelasanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.penjelasan_activity)
        supportActionBar?.title = "Penjelasan Hardware"
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PenjelasanFragment())
                .commitNow()
        }
    }

}

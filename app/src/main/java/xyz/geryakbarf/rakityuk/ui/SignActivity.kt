package xyz.geryakbarf.rakityuk.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.ui.fragment.LoginFragment

class SignActivity : AppCompatActivity() {

    private var kode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        supportActionBar?.hide()
        loadFragment()
        kode = intent.getIntExtra("kode", 0)

    }

    private fun loadFragment(){
        val t: FragmentManager = supportFragmentManager
        val r : FragmentTransaction = t.beginTransaction()
        val loginFragment = LoginFragment()
        r.replace(R.id.fragment, loginFragment)
        r.commit()
    }

    override fun onBackPressed() {
        when (kode) {
            0 -> super.onBackPressed()
            1 -> {
                val intent = Intent(applicationContext, MenuActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            2 -> {
                super.onBackPressed()
            }
        }
    }
}

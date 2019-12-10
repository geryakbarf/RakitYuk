package xyz.geryakbarf.rakityuk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.ui.fragment.LoginFragment

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        supportActionBar?.hide()
        loadFragment()

    }

    private fun loadFragment(){
        val t: FragmentManager = supportFragmentManager
        val r : FragmentTransaction = t.beginTransaction()
        val loginFragment = LoginFragment()
        r?.replace(R.id.fragment, loginFragment)
        r?.commit()
    }
}

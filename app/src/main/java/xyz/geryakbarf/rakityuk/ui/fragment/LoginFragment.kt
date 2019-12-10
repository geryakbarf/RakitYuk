package xyz.geryakbarf.rakityuk.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_login.*
import xyz.geryakbarf.rakityuk.R

class LoginFragment : Fragment(),View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnDaftar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnDaftar ->{
                val t : FragmentTransaction = this.fragmentManager!!.beginTransaction()
                val fragment = RegisterFragment()
                t.replace(R.id.fragment, fragment)
                t.commit()
            }
        }
    }

}
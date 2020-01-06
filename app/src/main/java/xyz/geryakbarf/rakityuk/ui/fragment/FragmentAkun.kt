package xyz.geryakbarf.rakityuk.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.utils.Session

class FragmentAkun : Fragment() {

    private lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = Session(view.context)
        checkLogin()
    }

    private fun checkLogin() {
        if (session.getValueBoolean("is_login") == false) {
            val t: FragmentTransaction = this.fragmentManager!!.beginTransaction()
            val fragment = LoginFragment()
            t.replace(R.id.nav_host_fragment, fragment)
            t.commit()
        }
    }

}
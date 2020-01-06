package xyz.geryakbarf.rakityuk.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_diskusi.*
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.ui.BuatThreadActivity
import xyz.geryakbarf.rakityuk.utils.Session

class DiskusiFragment : Fragment(), View.OnClickListener {

    private lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_diskusi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = Session(view.context)
        fabTambah.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fabTambah -> {
                if (session.getValueBoolean("is_login") == true) {
                    val intent = Intent(context, BuatThreadActivity::class.java)
                    context!!.startActivity(intent)
                } else {
                    val t: FragmentTransaction = this.fragmentManager!!.beginTransaction()
                    val fragment = LoginFragment()
                    t.replace(R.id.nav_host_fragment, fragment)
                    t.commit()
                }

            }
        }
    }

}
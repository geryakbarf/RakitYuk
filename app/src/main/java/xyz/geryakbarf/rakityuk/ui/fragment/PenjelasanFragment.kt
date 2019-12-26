package xyz.geryakbarf.rakityuk.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import xyz.geryakbarf.rakityuk.R

class PenjelasanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val kode = activity!!.intent.getIntExtra("kode", 0)
        return if (kode == 0)
            inflater.inflate(R.layout.fragment_mobo, container, false)
        else
            inflater.inflate(R.layout.penjelasan_fragment, container, false)
    }

}

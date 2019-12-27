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
        return when (activity!!.intent.getIntExtra("kode", 0)) {
            0 -> inflater.inflate(R.layout.fragment_mobo, container, false)
            1 -> inflater.inflate(R.layout.fragment_cpu, container, false)
            2 -> inflater.inflate(R.layout.fragment_gpu, container, false)
            3 -> inflater.inflate(R.layout.fragment_ram, container, false)
            else -> inflater.inflate(R.layout.penjelasan_fragment, container, false)
        }
    }

}

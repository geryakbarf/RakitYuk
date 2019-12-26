package xyz.geryakbarf.rakityuk.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.fragment_home.*
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.ui.PenjelasanActivity


class HomeFragment : Fragment(), View.OnClickListener {

    private val img = arrayListOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)
    private val imageListener =
        ImageListener { position, imageView -> imageView.setImageResource(img[position]) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        caroselView.setImageListener(imageListener)
        caroselView.pageCount = 3
        btnMobo.setOnClickListener(this)

    }

    private fun moveIntent(kode: Int) {
        val intent = Intent(context, PenjelasanActivity::class.java)
        intent.putExtra("kode", kode)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnMobo -> moveIntent(0)
        }
    }


}
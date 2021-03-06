package xyz.geryakbarf.rakityuk.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.utils.PageAdapter
import xyz.geryakbarf.rakityuk.utils.Session

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var progressDialog: ProgressDialog

    private lateinit var mPager: ViewPager
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: Array<ImageView>
    private lateinit var mAdapter: PageAdapter
    private var layouts: IntArray = intArrayOf(
        R.layout.slide1,
        R.layout.slide2,
        R.layout.slide3,
        R.layout.slide4
    )
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        session = Session(applicationContext)
        btnLogin.visibility = View.INVISIBLE
        btnSkip.visibility = View.INVISIBLE
        btnLogin.setOnClickListener(this)
        btnSkip.setOnClickListener(this)
        mPager = findViewById(R.id.pager)
        mAdapter = PageAdapter(layouts, this)
        mPager.adapter = mAdapter
        // button skip and next
        dotsLayout = findViewById(R.id.dots)
        createDots(0)
        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                createDots(p0)

                if (p0 == layouts.size - 1) {
                    btnLogin.visibility = View.VISIBLE
                    btnSkip.visibility = View.VISIBLE
                } else {
                    btnLogin.visibility = View.INVISIBLE
                    btnSkip.visibility = View.INVISIBLE
                }

            }

        })
        //Pengecekan Login
        isFirstime()

    }

    private fun moveActivity() {
        val intent = Intent(applicationContext, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isFirstime() {
        val isFirstime = session.getValueBoolean("is_firstime")
        if (isFirstime!!) {
            moveActivity()
        }
    }

    fun createDots(position: Int) {
        dotsLayout.removeAllViews()

        dots = Array(layouts.size) { ImageView(this) }

        for (i in layouts.indices) {

            dots[i] = ImageView(this)

            if (i == position) {
                dots[i].setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.active_dots
                    )
                )
            } else {
                dots[i].setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.inactive_dots
                    )
                )
            }

            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(4, 0, 4, 0)
            dotsLayout.addView(dots[i], params)


        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnLogin -> {
                val intent = Intent(applicationContext, SignActivity::class.java)
                startActivity(intent)
            }
            R.id.btnSkip -> {
                session.putBoolean("is_firstime", true)
                val intent = Intent(applicationContext, MenuActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


}

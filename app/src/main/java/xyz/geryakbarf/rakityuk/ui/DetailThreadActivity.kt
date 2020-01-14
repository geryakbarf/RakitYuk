@file:Suppress("DEPRECATION")

package xyz.geryakbarf.rakityuk.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_thread.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.api.GetLikeApi
import xyz.geryakbarf.rakityuk.api.ThreadApi
import xyz.geryakbarf.rakityuk.api.UnlikeApi
import xyz.geryakbarf.rakityuk.api.UpApi
import xyz.geryakbarf.rakityuk.models.BaseModels
import xyz.geryakbarf.rakityuk.models.ThreadModels
import xyz.geryakbarf.rakityuk.utils.Server
import xyz.geryakbarf.rakityuk.utils.Session

class DetailThreadActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_thread)
        supportActionBar?.hide()
        session = Session(applicationContext)
        progressDialog = ProgressDialog(this@DetailThreadActivity)
        progressDialog.setTitle("Memuat...")
        progressDialog.setCancelable(false)
        progressDialog.max = 5
        btnUp.setOnClickListener(this)
        initialState()
        getDetailThread()

    }

    private fun pengecekanLike() {
        if (!session.getValueString("username").isNullOrBlank() or !session.getValueString("username").isNullOrEmpty()) {
            val idPost = intent.getStringExtra("idPost")
            val username = session.getValueString("username")
            val retrofit = Retrofit.Builder().baseUrl(Server.url)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val getLike = retrofit.create(GetLikeApi::class.java)
            val response = getLike.getLike(idPost, username!!)
            response.enqueue(object : Callback<BaseModels> {
                override fun onFailure(call: Call<BaseModels>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(call: Call<BaseModels>, response: Response<BaseModels>) {
                    val kode: Int = response.body()!!.kode
                    if (kode > 0) {
                        btnUp.tag = "liked"
                        btnUp.setImageResource(R.drawable.liked)
                    }
                }
            })
        }
    }

    private fun initialState() {
        txtOP.text = intent.getStringExtra("username")
        txtJudul.text = intent.getStringExtra("title")
    }

    private fun getDetailThread() {
        progressDialog.show()
        imgGambar.visibility = View.GONE
        val idPost = intent.getStringExtra("idPost")
        val retrofit = Retrofit.Builder().baseUrl(Server.url)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val threadApi = retrofit.create(ThreadApi::class.java)
        val response = threadApi.getDetail(idPost)
        response.enqueue(object : Callback<ThreadModels> {
            override fun onFailure(call: Call<ThreadModels>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Terjadi kesalahan! Periksa koneksi anda!",
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressed()
            }

            override fun onResponse(call: Call<ThreadModels>, response: Response<ThreadModels>) {
                val kode = response.body()!!.kode
                if (kode > 0) {
                    progressDialog.dismiss()
                    pengecekanLike()
                    val img = response.body()?.image
                    if (!(img.isNullOrEmpty() or img.equals("-"))) {
                        imgGambar.visibility = View.VISIBLE
                        Glide.with(applicationContext).load(response.body()!!.image).into(imgGambar)
                    } else {
                        imgGambar.visibility = View.GONE
                    }
                    txtPostDate.text = response.body()!!.tanggal
                    txtIsi.text = response.body()!!.isi
                    txtJumlahViews.text = response.body()!!.views.toString()
                    txtJumlahUp.text = response.body()!!.likes.toString()
                    txtJumlahDown.text = response.body()!!.dislikes.toString()
                    Glide.with(applicationContext).load(response.body()!!.profile_image).into(imgOP)

                } else {
                    Toast.makeText(applicationContext, response.body()!!.pesan, Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
            }

        })
    }

    private fun upThread() {
        val idPost = intent.getStringExtra("idPost")
        val username = session.getValueString("username")

        if (btnUp.tag.toString() == "like") {
            val retrofit = Retrofit.Builder().baseUrl(Server.url)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val upApi = retrofit.create(UpApi::class.java)
            val response = upApi.likePost(idPost, username!!)
            response.enqueue(object : Callback<BaseModels> {
                override fun onFailure(call: Call<BaseModels>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Terjadi kesalahan saat memberi reputasi pada thread",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(call: Call<BaseModels>, response: Response<BaseModels>) {
                    val kode: Int = response.body()!!.kode
                    if (kode > 0) {
                        btnUp.tag = "liked"
                        btnUp.setImageResource(R.drawable.liked)
                        txtJumlahUp.text = (txtJumlahUp.text.toString().toInt() + 1).toString()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!.pesan,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })

        } else if (btnUp.tag.toString() == "liked") {
            val retrofit = Retrofit.Builder().baseUrl(Server.url)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val unlikeApi = retrofit.create(UnlikeApi::class.java)
            val response = unlikeApi.unlikeThread(idPost, username!!)
            response.enqueue(object : Callback<BaseModels> {
                override fun onFailure(call: Call<BaseModels>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Terjadi kesalahan saat memberi reputasi pada thread",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(call: Call<BaseModels>, response: Response<BaseModels>) {
                    val kode: Int = response.body()!!.kode
                    if (kode > 0) {
                        btnUp.tag = "like"
                        btnUp.setImageResource(R.drawable.up)
                        txtJumlahUp.text = (txtJumlahUp.text.toString().toInt() - 1).toString()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!.pesan,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnUp -> {
                if (session.getValueBoolean("is_login") == false) {
                    Toast.makeText(
                        applicationContext,
                        "Silahkan login untuk memberi reputasi pada thread",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                } else
                    upThread()
            }
        }
    }
}

@file:Suppress("DEPRECATION")

package xyz.geryakbarf.rakityuk.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_thread.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.adapter.AdapterKomentar
import xyz.geryakbarf.rakityuk.api.*
import xyz.geryakbarf.rakityuk.models.BaseModels
import xyz.geryakbarf.rakityuk.models.CommentsModels
import xyz.geryakbarf.rakityuk.models.ThreadModels
import xyz.geryakbarf.rakityuk.utils.Server
import xyz.geryakbarf.rakityuk.utils.Session

class DetailThreadActivity : AppCompatActivity(), View.OnClickListener,
    View.OnScrollChangeListener {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var session: Session
    private val url = Server.url3
    private var count = 1
    private val list: ArrayList<CommentsModels> = arrayListOf()
    private lateinit var requestQueue: RequestQueue
    private lateinit var adapterKomentar: AdapterKomentar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_thread)
        supportActionBar?.hide()
        rvKomentar.setOnScrollChangeListener(this)
        session = Session(applicationContext)
        progressDialog = ProgressDialog(this@DetailThreadActivity)
        progressDialog.setTitle("Memuat...")
        progressDialog.setCancelable(false)
        progressDialog.max = 5
        btnUp.setOnClickListener(this)
        btnComment.setOnClickListener(this)
        btnDown.setOnClickListener(this)
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

    private fun pengecekanDislike() {
        if (!session.getValueString("username").isNullOrBlank() or !session.getValueString("username").isNullOrEmpty()) {
            val idPost = intent.getStringExtra("idPost")
            val username = session.getValueString("username")
            val retrofit = Retrofit.Builder().baseUrl(Server.url)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val getDislike = retrofit.create(GetDislikeApi::class.java)
            val response = getDislike.getDislike(idPost, username!!)
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
                        btnDown.tag = "disliked"
                        btnDown.setImageResource(R.drawable.disliked)
                    }
                }
            })
        }
    }

    private fun initialState() {
        txtOP.text = intent.getStringExtra("username")
        txtJudul.text = intent.getStringExtra("title")
        session.putString("idpost", intent.getStringExtra("idPost"))
        session.putString("op", intent.getStringExtra("username"))
        session.putString("title", intent.getStringExtra("title"))
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
                    pengecekanDislike()
                    val img = response.body()?.image
                    if (!(img.isNullOrEmpty() or img.equals("-"))) {
                        imgGambar.visibility = View.VISIBLE
                        Glide.with(applicationContext).load(response.body()!!.image).into(imgGambar)
                    } else {
                        imgGambar.visibility = View.GONE
                    }
                    txtPostDate.text = response.body()!!.tanggal.substring(0, 10)
                    txtIsi.text = response.body()!!.isi
                    txtJumlahViews.text = response.body()!!.views.toString()
                    txtJumlahUp.text = response.body()!!.likes.toString()
                    txtJumlahDown.text = response.body()!!.dislikes.toString()
                    Glide.with(applicationContext).load(response.body()!!.profile_image).into(imgOP)
                    //Instansiasi Variabel untuk komentar
                    requestQueue = Volley.newRequestQueue(applicationContext)
                    rvKomentar.setHasFixedSize(true)
                    rvKomentar.layoutManager = LinearLayoutManager(applicationContext)
                    getData()
                    adapterKomentar = AdapterKomentar(list)
                    rvKomentar.adapter = adapterKomentar
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
                        if (btnDown.tag.toString() == "disliked")
                            downThread()
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

    private fun downThread() {
        val idPost = intent.getStringExtra("idPost")
        val username = session.getValueString("username")

        if (btnDown.tag.toString() == "dislike") {
            val retrofit = Retrofit.Builder().baseUrl(Server.url)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val downApi = retrofit.create(DownApi::class.java)
            val response = downApi.dislike(idPost, username!!)
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
                        btnDown.tag = "disliked"
                        btnDown.setImageResource(R.drawable.disliked)
                        txtJumlahDown.text = (txtJumlahDown.text.toString().toInt() + 1).toString()
                        if (btnUp.tag.toString() == "liked")
                            upThread()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!.pesan,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })

        } else if (btnDown.tag.toString() == "disliked") {
            val retrofit = Retrofit.Builder().baseUrl(Server.url)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val undownApi = retrofit.create(UndownAPi::class.java)
            val response = undownApi.unlikeThread(idPost, username!!)
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
                        btnDown.tag = "dislike"
                        btnDown.setImageResource(R.drawable.down)
                        txtJumlahDown.text = (txtJumlahDown.text.toString().toInt() - 1).toString()
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
            R.id.btnComment -> {
                val intent = Intent(applicationContext, ReplyActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.btnDown -> {
                if (session.getValueBoolean("is_login") == false) {
                    Toast.makeText(
                        applicationContext,
                        "Silahkan login untuk memberi reputasi pada thread",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                } else
                    downThread()
            }
        }
    }


    private fun parseData(array: JSONArray) {
        for (i in 0 until array.length()) {
            val ob = array.getJSONObject(i)
            //checking Null Data
            var likes = ob.getString("likes")
            if (likes == "null")
                likes = "0"
            var dislikes = ob.getString("dislikes")
            if (dislikes == "null")
                dislikes = "0"
            var status = ob.getString("status")
            if (status == "null")
                status = "null"
            var statusdis = ob.getString("statusdis")
            if (statusdis == "null")
                statusdis = "null"

            val listData = CommentsModels(
                ob.getString("username"),
                ob.getString("isi"),
                ob.getString("tanggal").substring(0, 10),
                likes,
                dislikes,
                ob.getString("img"),
                status,
                statusdis
            )
            list.add(listData)

        }
        adapterKomentar.notifyDataSetChanged()
    }

    private fun getDataFromServer(requestCount: Int): JsonArrayRequest {
        //Initializing IDPost
        val idPost = intent.getStringExtra("idPost")
        val username = session.getValueString("username")
        //Displaying Progressbar

        //Returning the request
        return JsonArrayRequest(
            "$url$requestCount&id=$idPost&user=$username",
            com.android.volley.Response.Listener { response ->
                //Calling method parseData to parse the json response
                parseData(response)
                //Hiding the progressbar
            },
            com.android.volley.Response.ErrorListener {
                //If an error occurs that means end of the list has reached
            })
    }

    private fun getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(count))
        //Incrementing the request counter
        count++
    }

    private fun isLastItemDisplaying(recyclerView: RecyclerView): Boolean {
        if (recyclerView.adapter!!.itemCount != 0) {
            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.adapter!!.itemCount - 1)
                return true
        }
        return false
    }

    override fun onScrollChange(
        v: View?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        if (isLastItemDisplaying(rvKomentar)) {
            //Calling the method getdata again
            getData()
        }
    }

}

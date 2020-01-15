package xyz.geryakbarf.rakityuk.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reply.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.api.ReplyApi
import xyz.geryakbarf.rakityuk.models.BaseModels
import xyz.geryakbarf.rakityuk.utils.Server
import xyz.geryakbarf.rakityuk.utils.Session

class ReplyActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var session: Session
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)
        supportActionBar?.hide()
        session = Session(applicationContext)
        //Inisialisasi ProgressDialog
        progressDialog = ProgressDialog(this@ReplyActivity)
        progressDialog.setTitle("Mengirim komentar")
        progressDialog.setCancelable(false)
        //Instansiasi Button Listener
        btnKirim.setOnClickListener(this)
        //Cek Intent
        if (!intent.getStringExtra("user").isNullOrEmpty()) {
            val user = "@" + intent.getStringExtra("user")
            etIsiKomen.setText(user)
        }


    }

    private fun kirimKomentar() {
        val isiKomen = etIsiKomen.text.toString().trim()
        if (TextUtils.isEmpty(isiKomen)) {
            etIsiKomen.error = "Isi komen tidak boleh kosong!"
            return
        }

        progressDialog.show()
        val idPost = session.getValueString("idpost")
        val op = session.getValueString("op")
        val title = session.getValueString("title")

        val retrofit = Retrofit.Builder().baseUrl(Server.url)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val replyApi = retrofit.create(ReplyApi::class.java)
        val response = replyApi.reply(idPost!!, session.getValueString("username")!!, isiKomen)
        response.enqueue(object : Callback<BaseModels> {
            override fun onFailure(call: Call<BaseModels>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Terjadi kesalahan! Periksa koneksi anda!",
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressed()
            }

            override fun onResponse(call: Call<BaseModels>, response: Response<BaseModels>) {
                val kode = response.body()!!.kode
                if (kode > 0) {
                    progressDialog.dismiss()
                    val intent = Intent(applicationContext, DetailThreadActivity::class.java)
                    intent.putExtra("idPost", idPost)
                    intent.putExtra("username", op)
                    intent.putExtra("title", title)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, response.body()!!.pesan, Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
            }

        })


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnKirim -> {
                kirimKomentar()
            }
        }
    }

    override fun onBackPressed() {
        val idPost = session.getValueString("idpost")
        val op = session.getValueString("op")
        val title = session.getValueString("title")
        val intent = Intent(applicationContext, DetailThreadActivity::class.java)
        intent.putExtra("idPost", idPost)
        intent.putExtra("username", op)
        intent.putExtra("title", title)
        startActivity(intent)
        finish()
    }

}

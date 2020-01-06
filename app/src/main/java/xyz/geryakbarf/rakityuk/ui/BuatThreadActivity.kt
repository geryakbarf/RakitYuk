package xyz.geryakbarf.rakityuk.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_buat_thread.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.api.InsertThreadApi
import xyz.geryakbarf.rakityuk.models.BaseModels
import xyz.geryakbarf.rakityuk.utils.Server
import xyz.geryakbarf.rakityuk.utils.Session

class BuatThreadActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var session: Session
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buat_thread)
        supportActionBar?.hide()
        session = Session(applicationContext)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Sedang Membuat Thread...")
        progressDialog.setCancelable(false)
        //SetOnClicListener
        btnBack.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnBack -> super.onBackPressed()
            R.id.btnSave -> upload()
        }
    }

    private fun upload() {
        val judul = etJudul.text.toString().trim()
        if (TextUtils.isEmpty(judul)) {
            etJudul.error = "Judul Tidak Boleh Kosong!"
            return
        }

        val isi = etIsiThread.text.toString()
        if (TextUtils.isEmpty(isi)) {
            etIsiThread.error = "Isi thread tidak boleh kosong!"
            return
        }

        val username = session.getValueString("username")

        progressDialog.show()

        val retrofit = Retrofit.Builder().baseUrl(Server.url)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val insertApi = retrofit.create(InsertThreadApi::class.java)
        val response = insertApi.insertThread(judul, isi, username!!)
        response.enqueue(object : Callback<BaseModels> {
            override fun onFailure(call: Call<BaseModels>, t: Throwable) {
                Toast.makeText(applicationContext, "Koneksi Error", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }

            override fun onResponse(call: Call<BaseModels>, response: Response<BaseModels>) {
                val kode = response.body()!!.kode
                if (kode == 1) {
                    Toast.makeText(applicationContext, response.body()!!.pesan, Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                    finish()
                    onBackPressed()
                } else {
                    Toast.makeText(applicationContext, response.body()!!.pesan, Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
            }

        })

    }
}

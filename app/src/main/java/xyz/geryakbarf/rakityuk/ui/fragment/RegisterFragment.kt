@file:Suppress("DEPRECATION")

package xyz.geryakbarf.rakityuk.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.api.RegisterApi
import xyz.geryakbarf.rakityuk.models.BaseModels
import xyz.geryakbarf.rakityuk.utils.Server

@Suppress("DEPRECATION")
class RegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set OnClickLinstener
        btnMasuk.setOnClickListener(this)
        btnDaftar.setOnClickListener(this)
        //Set ProgressDialog
        progressDialog = ProgressDialog(view.context)
        progressDialog.setMessage("Sedang Mendaftar...")
        progressDialog.setCancelable(false)
    }

    private fun moveFragment() {
        val t: FragmentTransaction = this.fragmentManager!!.beginTransaction()
        val fragment = LoginFragment()
        t.replace(R.id.fragment, fragment)
        t.commit()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnMasuk -> moveFragment()
            R.id.btnDaftar -> register()
        }
    }

    private fun register() {
        val email = etEmail.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            etEmail.error = "Email tidak boleh kosong!"
            return
        }

        val username = etUsername.text.toString().trim()
        if (TextUtils.isEmpty(username)) {
            etUsername.error = "Username tidak boleh kosong!"
            return
        }

        if (username.length < 5) {
            etUsername.error = "Username minimal harus 5 karakter!"
            return
        }

        val password = etPassword.text.toString().trim()
        if (TextUtils.isEmpty(password)) {
            etPassword.error = "Password tidak boleh kosong!"
            return
        }

        val konfirmasi = etKonfirmasi.text.toString().trim()
        if (TextUtils.isEmpty(konfirmasi)) {
            etKonfirmasi.error = "Konfirmasi password tidak boleh kosong!"
            return
        }

        if (password != konfirmasi) {
            etKonfirmasi.error = "Konfirmasi password harus sama dengan password!"
            return
        }

        progressDialog.show()

        val retrofit = Retrofit.Builder().baseUrl(Server.url)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val registerApi = retrofit.create(RegisterApi::class.java)
        val response = registerApi.register(email, username, password)
        response.enqueue(object : Callback<BaseModels> {

            override fun onFailure(call: Call<BaseModels>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(context, "Koneksi bermasalah!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<BaseModels>, response: Response<BaseModels>) {
                val kode = response.body()!!.kode
                if (kode > 0) {
                    progressDialog.dismiss()
                    //Pindah Ke Fragment Login
                    Toast.makeText(context, response.body()?.pesan, Toast.LENGTH_SHORT).show()
                    moveFragment()

                } else {
                    progressDialog.dismiss()
                    Toast.makeText(context, response.body()!!.pesan, Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

}
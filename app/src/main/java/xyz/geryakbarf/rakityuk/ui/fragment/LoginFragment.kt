@file:Suppress("DEPRECATION")

package xyz.geryakbarf.rakityuk.ui.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.api.LoginApi
import xyz.geryakbarf.rakityuk.models.LoginModels
import xyz.geryakbarf.rakityuk.ui.MenuActivity
import xyz.geryakbarf.rakityuk.utils.Server
import xyz.geryakbarf.rakityuk.utils.Session

@Suppress("DEPRECATION")
class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set Button Listener
        btnDaftar.setOnClickListener(this)
        btnMasuk.setOnClickListener(this)
        //Set Progress Dialog
        progressDialog = ProgressDialog(view.context)
        progressDialog.setMessage("Sedang Masuk...")
        progressDialog.setCancelable(false)
        //Set Session
        session = Session(view.context)
    }

    private fun login() {
        val username = etUsername.text.toString().trim()
        if (TextUtils.isEmpty(username)) {
            etUsername.error = "Username tidak boleh kosong!"
            return
        }

        val password = etPassword.text.toString().trim()
        if (TextUtils.isEmpty(password)) {
            etPassword.error = "Password tidak boleh kosong!"
            return
        }

        progressDialog.show()

        val retrofit = Retrofit.Builder().baseUrl(Server.url)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val loginApi = retrofit.create(LoginApi::class.java)
        val response = loginApi.login(username, password)
        response.enqueue(object : Callback<LoginModels> {

            override fun onFailure(call: Call<LoginModels>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(context, "Koneksi bermasalah!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoginModels>, response: Response<LoginModels>) {
                val kode = response.body()!!.kode
                if (kode > 0) {
                    progressDialog.dismiss()
                    //Menyimpan Data ke Session
                    session.putString("username", username)
                    session.putString("email", response.body()!!.email)
                    session.putString("profile_image", response.body()!!.img)
                    session.putString("point", response.body()!!.point.toString())
                    session.putString("join_date", response.body()!!.joinDate)
                    session.putBoolean("is_login", true)
                    session.putBoolean("is_firstime", true)
                    //Pindah Ke Activity Yang Baru
                    moveActivity()

                } else {
                    progressDialog.dismiss()
                    Toast.makeText(context, response.body()!!.pesan, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnDaftar -> {
                val t: FragmentTransaction = this.fragmentManager!!.beginTransaction()
                val fragment = RegisterFragment()
                t.replace(R.id.fragment, fragment)
                t.commit()
            }
            R.id.btnMasuk -> login()
        }
    }

    private fun moveActivity() {
        val intent = Intent(view!!.context, MenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity!!.startActivity(intent)
        activity!!.finish()
    }


}
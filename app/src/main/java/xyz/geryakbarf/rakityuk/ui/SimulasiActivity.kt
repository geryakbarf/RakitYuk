package xyz.geryakbarf.rakityuk.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_simulasi.*
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.adapter.AdapterSocket
import xyz.geryakbarf.rakityuk.models.SocketModels
import xyz.geryakbarf.rakityuk.utils.DBUtils

class SimulasiActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var db: DBUtils
    private lateinit var list: ArrayList<SocketModels>
    private lateinit var socket: String
    private lateinit var adapterSocket: AdapterSocket

    private var merek: String = ""
    private var hargaCPU = 0
    private var hargaMobo = 0
    private var hargaRam1 = 0
    private var ddr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulasi)
        supportActionBar?.hide()

        list = arrayListOf()
        db = DBUtils(applicationContext)
        btnPilihMobo.setOnClickListener(this)
        btnPilihCpu.setOnClickListener(this)
        btnPilihRam1.setOnClickListener(this)

        //Deklarasi Spinner
        spinnerSocket.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do Nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                socket = list[position].socket
            }

        }

        //Radio Group Listener
        rgCpu.setOnCheckedChangeListener { group, checkedId ->
            if (rbIntel.isChecked) {
                getSocket("LGA")
                merek = "Intel"
            } else {
                getSocket("M")
                merek = "AMD"
            }
        }

        adapterSocket = AdapterSocket(list, this@SimulasiActivity)
        spinnerSocket.adapter = adapterSocket
        getSocket("LGA")
    }

    private fun getSocket(merek: String) {
        list.clear()
        val res = db.readSocket(merek)
        while (res.moveToNext()) {
            val item = SocketModels()
            item.socket = res.getString(0)
            list.add(item)
        }
        adapterSocket.notifyDataSetChanged()
    }

    private fun hitung() {
        txtHarga.text = "Rp. " + (hargaCPU + hargaMobo + hargaRam1).toString()
    }

    private fun changeState() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                btnPilihMobo.text = data?.getStringExtra("nama")
                hargaMobo = data!!.getStringExtra("harga").toInt()
                ddr = data.getStringExtra("ddr")
                hitung()
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                btnPilihCpu.text = data?.getStringExtra("nama")
                hargaCPU = data!!.getStringExtra("harga").toInt()
                hitung()
            }
        } else if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                btnPilihRam1.text = data?.getStringExtra("nama")
                hargaRam1 = data!!.getStringExtra("harga").toInt()
                hitung()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnPilihMobo -> {
                val intent = Intent(applicationContext, KomponenActivity::class.java)
                intent.putExtra("kode", 1)
                intent.putExtra("socket", socket)
                startActivityForResult(intent, 1)
            }
            R.id.btnPilihCpu -> {
                val intent = Intent(applicationContext, KomponenActivity::class.java)
                intent.putExtra("kode", 2)
                intent.putExtra("socket", socket)
                startActivityForResult(intent, 2)
            }
            R.id.btnPilihRam1 -> {
                if (ddr == "")
                    Toast.makeText(
                        applicationContext,
                        "Silahkan pilih Motherboard terlebih dahulu!",
                        Toast.LENGTH_SHORT
                    ).show()
                else {
                    val intent = Intent(applicationContext, KomponenActivity::class.java)
                    intent.putExtra("kode", 3)
                    intent.putExtra("ddr", ddr)
                    startActivityForResult(intent, 3)
                }
            }
        }
    }
}

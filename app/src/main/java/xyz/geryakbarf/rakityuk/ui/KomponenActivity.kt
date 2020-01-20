package xyz.geryakbarf.rakityuk.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_komponen.*
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.adapter.AdapterKomponen
import xyz.geryakbarf.rakityuk.models.KomponenModels
import xyz.geryakbarf.rakityuk.utils.DBUtils

class KomponenActivity : AppCompatActivity() {

    private lateinit var list: MutableList<KomponenModels>
    private lateinit var adapter: AdapterKomponen
    private lateinit var db: DBUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_komponen)

        list = arrayListOf()
        db = DBUtils(applicationContext)
        adapter = AdapterKomponen(list, this@KomponenActivity)
        rvKomponen.adapter = adapter
        intentAction()

        //ListView onclick listener
        rvKomponen.setOnItemClickListener { parent, view, position, id ->
            if (intent.getIntExtra("kode", 0) == 1) {
                val resulintent = Intent()
                resulintent.putExtra("nama", list[position].nama)
                resulintent.putExtra("harga", list[position].harga)
                resulintent.putExtra("ddr", list[position].ddr)
                setResult(Activity.RESULT_OK, resulintent)
                finish()
            } else if (intent.getIntExtra("kode", 0) == 2) {
                val resulintent = Intent()
                resulintent.putExtra("nama", list[position].nama)
                resulintent.putExtra("harga", list[position].harga)
                setResult(Activity.RESULT_OK, resulintent)
                finish()
            } else {
                val resulintent = Intent()
                resulintent.putExtra("nama", list[position].nama)
                resulintent.putExtra("harga", list[position].harga)
                setResult(Activity.RESULT_OK, resulintent)
                finish()
            }
        }
    }

    private fun intentAction() {
        if (intent.getIntExtra("kode", 0) == 1) {
            list.clear()
            val res = db.readMobo(intent.getStringExtra("socket"))
            while (res.moveToNext()) {
                val item = KomponenModels()
                item.nama = res.getString(0)
                item.harga = removedot(res.getString(3))
                item.ddr = res.getString(2)
                list.add(item)
            }
            adapter.notifyDataSetChanged()
        } else if (intent.getIntExtra("kode", 0) == 2) {
            list.clear()
            val res = db.readCPU(intent.getStringExtra("socket"))
            while (res.moveToNext()) {
                val item = KomponenModels()
                item.nama = res.getString(0)
                item.harga = removedot(res.getString(3))
                list.add(item)
            }
            adapter.notifyDataSetChanged()
        } else if (intent.getIntExtra("kode", 0) == 3) {
            list.clear()
            val res = db.readMemory(intent.getStringExtra("ddr"))
            while (res.moveToNext()) {
                val item = KomponenModels()
                item.nama = res.getString(0)
                item.harga = removedot(res.getString(2))
                list.add(item)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun removedot(query: String): String {
        val angkaDemo = query.replace(".", "")
        return angkaDemo.substring(0, angkaDemo.length - 1)
    }

    companion object {
        var nama: String = ""
        var ddr: String = ""
        var harga: String = ""
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }
}

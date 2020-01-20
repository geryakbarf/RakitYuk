package xyz.geryakbarf.rakityuk.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.models.KomponenModels

class AdapterKomponen : BaseAdapter {
    lateinit var listdata: MutableList<KomponenModels>
    lateinit var inflater: LayoutInflater
    lateinit var activiy: Activity

    constructor(listdata: MutableList<KomponenModels>, activiy: Activity) : super() {
        this.activiy = activiy
        this.listdata = listdata
        this.inflater = LayoutInflater.from(activiy)
    }

    override fun getCount(): Int {
        return listdata.size
    }

    override fun getItem(position: Int): Any {
        return listdata.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (inflater == null)
            inflater = activiy.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (convertView == null)
            convertView = inflater.inflate(R.layout.adapter_komponen, null)

        val txtNamaKomponen = convertView?.findViewById(R.id.txtNamaKomponen) as TextView
        val txtHargaKomponen = convertView.findViewById(R.id.txtHargaKomponen) as TextView

        val data: KomponenModels = listdata[position]

        txtNamaKomponen.text = data.nama
        txtHargaKomponen.text = "Rp. " + data.harga


        return convertView
    }
}
package xyz.geryakbarf.rakityuk.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.models.SocketModels

class AdapterSocket : BaseAdapter {
    lateinit var listdata: MutableList<SocketModels>
    lateinit var inflater: LayoutInflater
    lateinit var activiy: Activity

    constructor(listdata: MutableList<SocketModels>, activiy: Activity) : super() {
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
            convertView = inflater.inflate(R.layout.adapter_socket, null)

        val txtNamaRute = convertView?.findViewById(R.id.txtSocket) as TextView

        val data: SocketModels
        data = listdata.get(position)

        txtNamaRute.text = data.socket


        return convertView
    }
}
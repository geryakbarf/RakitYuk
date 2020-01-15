package xyz.geryakbarf.rakityuk.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.models.CommentsModels
import xyz.geryakbarf.rakityuk.ui.ReplyActivity

class AdapterKomentar(private val list: ArrayList<CommentsModels>) :
    RecyclerView.Adapter<AdapterKomentar.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_komentar,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (username, isi, tanggal, likes, dislikes, img, status, statusdis) = list[position]
        holder.username.text = username
        holder.isi.text = isi
        holder.likes.text = likes
        holder.dislikes.text = dislikes
        holder.tanggal.text = tanggal
        Glide.with(holder.itemView.context).load(img).into(holder.imgOP)
        holder.statusLike = status
        if (holder.statusLike != "null") {
            holder.btnUp.tag = "liked"
            holder.btnUp.setImageResource(R.drawable.liked)
        }
        holder.statusdis = statusdis
        if (holder.statusdis != "null") {
            holder.btnDown.tag = "disliked"
            holder.btnDown.setImageResource(R.drawable.disliked)
        }

        holder.btnKomen.setOnClickListener {
            val intent = Intent(holder.itemView.context, ReplyActivity::class.java)
            intent.putExtra("user", username)
            holder.itemView.context.startActivity(intent)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var statusLike: String = "1"
        var statusdis: String = "1"
        var username: TextView = itemView.findViewById(R.id.txtOP)
        var isi: TextView = itemView.findViewById(R.id.txtIsi)
        var likes: TextView = itemView.findViewById(R.id.txtJumlahUp)
        var dislikes: TextView = itemView.findViewById(R.id.txtJumlahDown)
        var imgOP: ImageView = itemView.findViewById(R.id.imgOP)
        var tanggal: TextView = itemView.findViewById(R.id.txtPostDate)
        var btnUp: ImageView = itemView.findViewById(R.id.btnUp)
        var btnDown: ImageView = itemView.findViewById(R.id.btnDown)
        var btnKomen: ImageView = itemView.findViewById(R.id.btnComment)
    }
}
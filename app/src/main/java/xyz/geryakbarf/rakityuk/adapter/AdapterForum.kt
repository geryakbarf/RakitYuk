package xyz.geryakbarf.rakityuk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.models.ForumModels

class AdapterForum(private val list: ArrayList<ForumModels>) :
    RecyclerView.Adapter<AdapterForum.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_forum,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (username, id, title, views, likes, dislikes, comments) = list[position]
        holder.idPost = id
        holder.username.text = username
        holder.title.text = title
        holder.likes.text = likes
        holder.dislikes.text = dislikes
        holder.comments.text = comments
        holder.views.text = views
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var idPost: String = ""
        var username: TextView = itemView.findViewById(R.id.txtUsername)
        var title: TextView = itemView.findViewById(R.id.txtJudulThread)
        var likes: TextView = itemView.findViewById(R.id.txtJumlahUp)
        var dislikes: TextView = itemView.findViewById(R.id.txtJumlahDown)
        var comments: TextView = itemView.findViewById(R.id.txtJumlahComment)
        var views: TextView = itemView.findViewById(R.id.txtJumlahViews)

    }
}
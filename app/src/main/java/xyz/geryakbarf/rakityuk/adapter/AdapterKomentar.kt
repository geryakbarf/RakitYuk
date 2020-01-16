package xyz.geryakbarf.rakityuk.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_forum.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.api.DownComment
import xyz.geryakbarf.rakityuk.api.UndownComment
import xyz.geryakbarf.rakityuk.api.UnlikeComment
import xyz.geryakbarf.rakityuk.api.UpComment
import xyz.geryakbarf.rakityuk.models.BaseModels
import xyz.geryakbarf.rakityuk.models.CommentsModels
import xyz.geryakbarf.rakityuk.ui.ReplyActivity
import xyz.geryakbarf.rakityuk.utils.Server
import xyz.geryakbarf.rakityuk.utils.Session

class AdapterKomentar(private val list: ArrayList<CommentsModels>) :
    RecyclerView.Adapter<AdapterKomentar.ViewHolder>() {

    private lateinit var session: Session

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
        val (id, username, isi, tanggal, likes, dislikes, img, status, statusdis) = list[position]
        session = Session(holder.itemView.context)
        holder.id = id
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

        //Aksi ketika menekan tombol like (Panjang cuk methodna)
        holder.btnUp.setOnClickListener {
            if (session.getValueBoolean("is_login") == false) {
                Toast.makeText(
                    holder.itemView.context,
                    "Silahkan login untuk memberi reputasi pada thread",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val un = session.getValueString("username")
                if (holder.btnUp.tag.toString() == "like") {
                    val retrofit = Retrofit.Builder().baseUrl(Server.url)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                    val upApi = retrofit.create(UpComment::class.java)
                    val response = upApi.likeComment(id, un!!)
                    response.enqueue(object : Callback<BaseModels> {
                        override fun onFailure(call: Call<BaseModels>, t: Throwable) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Terjadi kesalahan saat memberi reputasi pada thread",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<BaseModels>,
                            response: Response<BaseModels>
                        ) {
                            val kode: Int = response.body()!!.kode
                            if (kode > 0) {
                                holder.btnUp.tag = "liked"
                                holder.btnUp.setImageResource(R.drawable.liked)
                                holder.likes.text =
                                    (holder.likes.text.toString().toInt() + 1).toString()
                                if (holder.btnDown.tag.toString() == "disliked") {
                                    val retrofit = Retrofit.Builder().baseUrl(Server.url)
                                        .addConverterFactory(GsonConverterFactory.create()).build()
                                    val undownApi = retrofit.create(UndownComment::class.java)
                                    val response = undownApi.unlikeComment(id, un)
                                    response.enqueue(object : Callback<BaseModels> {
                                        override fun onFailure(
                                            call: Call<BaseModels>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(
                                                holder.itemView.context,
                                                "Terjadi kesalahan saat memberi reputasi pada komentar",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        override fun onResponse(
                                            call: Call<BaseModels>,
                                            response: Response<BaseModels>
                                        ) {
                                            val kode: Int = response.body()!!.kode
                                            if (kode > 0) {
                                                holder.btnDown.tag = "dislike"
                                                holder.btnDown.setImageResource(R.drawable.down)
                                                holder.dislikes.text =
                                                    (holder.dislikes.text.toString().toInt() - 1).toString()
                                            } else {
                                                Toast.makeText(
                                                    holder.itemView.context,
                                                    response.body()!!.pesan,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                    })

                                }
                            } else {
                                Toast.makeText(
                                    holder.itemView.context,
                                    response.body()!!.pesan,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    })

                } else if (holder.btnUp.tag.toString() == "liked") {
                    val retrofit = Retrofit.Builder().baseUrl(Server.url)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                    val unlikeApi = retrofit.create(UnlikeComment::class.java)
                    val response = unlikeApi.unlikeComment(id, un!!)
                    response.enqueue(object : Callback<BaseModels> {
                        override fun onFailure(call: Call<BaseModels>, t: Throwable) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Terjadi kesalahan saat memberi reputasi pada thread",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<BaseModels>,
                            response: Response<BaseModels>
                        ) {
                            val kode: Int = response.body()!!.kode
                            if (kode > 0) {
                                holder.btnUp.tag = "like"
                                holder.btnUp.setImageResource(R.drawable.up)
                                holder.likes.text =
                                    (holder.likes.text.toString().toInt() - 1).toString()
                            } else {
                                Toast.makeText(
                                    holder.itemView.context,
                                    response.body()!!.pesan,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    })
                }
            }
        }

        holder.itemView.btnDown.setOnClickListener {
            if (session.getValueBoolean("is_login") == false) {
                Toast.makeText(
                    holder.itemView.context,
                    "Silahkan login untuk memberi reputasi pada thread",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val un = session.getValueString("username")

                if (holder.btnDown.tag.toString() == "dislike") {
                    val retrofit = Retrofit.Builder().baseUrl(Server.url)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                    val downApi = retrofit.create(DownComment::class.java)
                    val response = downApi.dislike(id, un!!)
                    response.enqueue(object : Callback<BaseModels> {
                        override fun onFailure(call: Call<BaseModels>, t: Throwable) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Terjadi kesalahan saat memberi reputasi pada thread",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<BaseModels>,
                            response: Response<BaseModels>
                        ) {
                            val kode: Int = response.body()!!.kode
                            if (kode > 0) {
                                holder.btnDown.tag = "disliked"
                                holder.btnDown.setImageResource(R.drawable.disliked)
                                holder.dislikes.text =
                                    (holder.dislikes.text.toString().toInt() + 1).toString()
                                if (holder.btnUp.tag.toString() == "liked") {
                                    val retrofit = Retrofit.Builder().baseUrl(Server.url)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build()
                                    val unlikeApi = retrofit.create(UnlikeComment::class.java)
                                    val response = unlikeApi.unlikeComment(id, un)
                                    response.enqueue(object : Callback<BaseModels> {
                                        override fun onFailure(
                                            call: Call<BaseModels>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(
                                                holder.itemView.context,
                                                "Terjadi kesalahan saat memberi reputasi pada thread",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        override fun onResponse(
                                            call: Call<BaseModels>,
                                            response: Response<BaseModels>
                                        ) {
                                            val kode: Int = response.body()!!.kode
                                            if (kode > 0) {
                                                holder.btnUp.tag = "like"
                                                holder.btnUp.setImageResource(R.drawable.up)
                                                holder.likes.text =
                                                    (holder.likes.text.toString().toInt() - 1).toString()
                                            } else {
                                                Toast.makeText(
                                                    holder.itemView.context,
                                                    response.body()!!.pesan,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                    })
                                }
                            } else {
                                Toast.makeText(
                                    holder.itemView.context,
                                    response.body()!!.pesan,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    })

                } else if (holder.btnDown.tag.toString() == "disliked") {
                    val retrofit = Retrofit.Builder().baseUrl(Server.url)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                    val undownApi = retrofit.create(UndownComment::class.java)
                    val response = undownApi.unlikeComment(id, username)
                    response.enqueue(object : Callback<BaseModels> {
                        override fun onFailure(call: Call<BaseModels>, t: Throwable) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Terjadi kesalahan saat memberi reputasi pada thread",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<BaseModels>,
                            response: Response<BaseModels>
                        ) {
                            val kode: Int = response.body()!!.kode
                            if (kode > 0) {
                                holder.btnDown.tag = "dislike"
                                holder.btnDown.setImageResource(R.drawable.down)
                                holder.dislikes.text =
                                    (holder.dislikes.text.toString().toInt() - 1).toString()
                            } else {
                                Toast.makeText(
                                    holder.itemView.context,
                                    response.body()!!.pesan,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    })
                }
            }


        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var statusLike: String = "1"
        var statusdis: String = "1"
        var id: String = ""
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
package xyz.geryakbarf.rakityuk.ui.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_akun.*
import org.json.JSONArray
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.adapter.AdapterForum
import xyz.geryakbarf.rakityuk.models.ForumModels
import xyz.geryakbarf.rakityuk.ui.SignActivity
import xyz.geryakbarf.rakityuk.utils.Server
import xyz.geryakbarf.rakityuk.utils.Session

class FragmentAkun : Fragment(), View.OnScrollChangeListener {

    private lateinit var session: Session
    private val url = Server.url2
    private var count = 1
    private val list: ArrayList<ForumModels> = arrayListOf()
    private lateinit var requestQueue: RequestQueue
    private lateinit var adapterForum: AdapterForum
    private lateinit var progressBar: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = Session(view.context)
        checkLogin()
        progressBar = ProgressDialog(view.context)
        progressBar.setTitle("Memuat Thread...")
        progressBar.max = 5
        progressBar.setCancelable(false)
        requestQueue = Volley.newRequestQueue(context)
        rvPostingan.setHasFixedSize(true)
        rvPostingan.layoutManager = LinearLayoutManager(context)
        rvPostingan.setOnScrollChangeListener(this)
        rvPostingan.isNestedScrollingEnabled = false
        getData()
        progressBar.show()
        adapterForum = AdapterForum(list)
        rvPostingan.adapter = adapterForum
    }

    @SuppressLint("SetTextI18n")
    private fun initialState() {
        txtEmail.text = session.getValueString("email")
        txtUsername.text = session.getValueString("username")
        Glide.with(view!!.context).load(session.getValueString("profile_image")).into(imgUser)
        txtJoinDate.text = "Bergabung pada " + session.getValueString("join_date")
    }

    private fun checkLogin() {
        if (session.getValueBoolean("is_login") == false) {
            val intent = Intent(context, SignActivity::class.java)
            intent.putExtra("kode", 1)
            context!!.startActivity(intent)
        } else {
            initialState()
        }
    }

    private fun parseData(array: JSONArray) {
        for (i in 0 until array.length()) {
            val ob = array.getJSONObject(i)
            //checking Null Data
            var likes = ob.getString("likes")
            if (likes == "null")
                likes = "0"
            var dislikes = "0"
            if (dislikes == "null")
                dislikes = "0"
            var comments = "0"
            if (comments == "null")
                comments = "0"

            val listData = ForumModels(
                ob.getString("username"),
                ob.getString("id"),
                ob.getString("title"),
                ob.getString("views"),
                likes,
                dislikes,
                comments
            )
            list.add(listData)
            progressBar.dismiss()

        }
        adapterForum.notifyDataSetChanged()
    }

    private fun getDataFromServer(requestCount: Int): JsonArrayRequest {
        //Initializing ProgressBar

        //Displaying Progressbar

        //Returning the request
        return JsonArrayRequest(url + requestCount.toString() + "&user=" + session.getValueString("username"),
            Response.Listener { response ->
                //Calling method parseData to parse the json response
                parseData(response)
                //Hiding the progressbar
                progressBar.dismiss()
            },
            Response.ErrorListener {
                progressBar.dismiss()
                //If an error occurs that means end of the list has reached
            })
    }

    private fun getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(count))
        //Incrementing the request counter
        count++
    }

    private fun isLastItemDisplaying(recyclerView: RecyclerView): Boolean {
        if (recyclerView.adapter!!.itemCount != 0) {
            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.adapter!!.itemCount - 1)
                return true
        }
        return false
    }

    override fun onScrollChange(
        v: View?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        if (isLastItemDisplaying(rvPostingan)) {
            //Calling the method getdata again
            getData()
        }
    }

}
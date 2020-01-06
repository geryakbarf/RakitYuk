package xyz.geryakbarf.rakityuk.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_diskusi.*
import org.json.JSONArray
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.adapter.AdapterForum
import xyz.geryakbarf.rakityuk.models.ForumModels
import xyz.geryakbarf.rakityuk.ui.BuatThreadActivity
import xyz.geryakbarf.rakityuk.utils.Session

class DiskusiFragment : Fragment(), View.OnClickListener, View.OnScrollChangeListener {

    private lateinit var session: Session
    private val url = "https://geryakbarf.000webhostapp.com/get_all_thread.php?page="
    private var count = 1
    private val list: ArrayList<ForumModels> = arrayListOf()
    private lateinit var requestQueue: RequestQueue
    private lateinit var adapterForum: AdapterForum

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_diskusi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = Session(view.context)
        fabTambah.setOnClickListener(this)
        requestQueue = Volley.newRequestQueue(context)
        rvThread.setHasFixedSize(true)
        rvThread.layoutManager = LinearLayoutManager(context)
        rvThread.setOnScrollChangeListener(this)
        getData()
        progressBarForum.visibility = View.VISIBLE
        adapterForum = AdapterForum(list)
        rvThread.adapter = adapterForum
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fabTambah -> {
                if (session.getValueBoolean("is_login") == true) {
                    val intent = Intent(context, BuatThreadActivity::class.java)
                    context!!.startActivity(intent)
                } else {
                    val t: FragmentTransaction = this.fragmentManager!!.beginTransaction()
                    val fragment = LoginFragment()
                    t.replace(R.id.nav_host_fragment, fragment)
                    t.commit()
                }

            }
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
            progressBarForum.visibility = View.GONE

        }
        adapterForum.notifyDataSetChanged()
    }

    private fun getDataFromServer(requestCount: Int): JsonArrayRequest {
        //Initializing ProgressBar

        //Displaying Progressbar

        //Returning the request
        return JsonArrayRequest(url + requestCount.toString(),
            Response.Listener { response ->
                //Calling method parseData to parse the json response
                parseData(response)
                //Hiding the progressbar
                progressBarForum.visibility = View.GONE
            },
            Response.ErrorListener {
                progressBarForum.visibility = View.GONE
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
        if (isLastItemDisplaying(rvThread)) {
            //Calling the method getdata again
            getData()
        }
    }

}
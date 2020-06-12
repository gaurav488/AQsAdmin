package com.example.aqsadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aqsadmin.Adapters.bidders_list_adapter
import com.example.aqsadmin.models.bidder_list_model
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class bidders_list : AppCompatActivity() {
    private var recyclerview: RecyclerView? = null
    lateinit var bidder_list: ArrayList<bidder_list_model>
    private var linear: LinearLayoutManager? = null
    private var adapter: bidders_list_adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bidders_list)
                var auctid = getIntent().getExtras()?.get("auct_id").toString()
                recyclerview=findViewById(R.id.biiders_list_recycler)
                bidder_list=ArrayList<bidder_list_model>()
                val  dbRef = FirebaseDatabase.getInstance().getReference().child("auction").child(auctid).child("biding")
                dbRef.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for (ds: DataSnapshot in p0.children)
                        {
                            try {
                                var m=bidder_list_model()
                                m.username=ds.child("username").value.toString()
                                m.current_date_time=ds.child("current_date_time").value.toString()
                                m.bid_amount=ds.child("bid_amount").value.toString()
                                bidder_list.add(m)
                                //bidder_list!!.sortBy { it.bid_amount }
                                Collections.sort(bidder_list,
                                    Comparator { o1, o2 ->
                                        o1.current_date_time.compareTo(o2.current_date_time)
                                    })
                                bidder_list.reverse()
                                Log.d("data",ds.child("item_description").value.toString())
                            }catch (e: Exception)
                            {
                                e.printStackTrace()
                            }
                        }
                        adapter!!.update(bidder_list!!)
                    }
                })
                linear =
                    LinearLayoutManager(applicationContext)
                recyclerview?.layoutManager = linear
                recyclerview?.setHasFixedSize(true)
                adapter = bidders_list_adapter(applicationContext,bidder_list!!)
                recyclerview?.adapter = adapter
    }
}

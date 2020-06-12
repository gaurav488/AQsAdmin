package com.example.aqsadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.aqsadmin.Adapters.past_auction_adapter
import com.example.aqsadmin.models.past_auct_model
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class past_auction : AppCompatActivity() {
    private var past_recycler: RecyclerView? = null
    private var past_griditem: ArrayList<past_auct_model>? = null
    private var past_auction_adapter: past_auction_adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_auction)
        past_recycler = findViewById(R.id.past_recycler)
        past_griditem = ArrayList<past_auct_model>()

        val  dbRef = FirebaseDatabase.getInstance().getReference().child("history")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (ds: DataSnapshot in p0.children)
                {
                    try {
                        var s= past_auct_model()
                        s.item_description = ds.child("item_description").value.toString()
                        s.item_name = ds.child("item_name").value.toString()
                        s.item_amt = ds.child("item_amt").value.toString()
                        s.auct_end_date = ds.child("auct_end_date").value.toString()
                        s.auct_end_time = ds.child("auct_end_time").value.toString()
                        s.url = ds.child("url").value.toString()
                        s.estimated_end_bid=ds.child("estimated_end_bid").value.toString()
                        s.auct_id = ds.child("auct_id").value.toString()
                        past_griditem!!.add(s)

                    }catch (e: Exception)
                    {
                        e.printStackTrace()
                    }

                }
                past_auction_adapter!!.update(past_griditem!!)
            }
        })
        past_recycler?.layoutManager = LinearLayoutManager(this@past_auction)
        past_auction_adapter = past_auction_adapter(applicationContext, past_griditem!!)
        past_recycler?.adapter = past_auction_adapter
    }
}

package com.example.aqsadmin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aqsadmin.Adapters.auct_adapter
import com.example.aqsadmin.models.auct_model
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class active_auctions :  Fragment() {
    private var recyclView: RecyclerView? = null
    lateinit var auc_griditem: ArrayList<auct_model>
    private var linear: LinearLayoutManager? = null
    lateinit var adapter: auct_adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_active_auctions, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        recyclView = view.findViewById(R.id.auc_recy)
        auc_griditem = ArrayList<auct_model>()
        val sear = view.findViewById<SearchView>(R.id.search_card)
        linear = LinearLayoutManager(context)
        recyclView?.layoutManager = linear
        recyclView?.setHasFixedSize(true)
        adapter = auct_adapter(context, auc_griditem!!)
        recyclView?.adapter = adapter
        val dbRef = FirebaseDatabase.getInstance().getReference().child("auction")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                auc_griditem!!.clear()
                if (p0.exists()) {
                    for (ds: DataSnapshot in p0.children) {
                        try {
                            var m = auct_model()
                            m.item_description = ds.child("item_description").value.toString()
                            m.item_name = ds.child("item_name").value.toString()
                            m.item_amt = ds.child("item_amt").value.toString()
                            m.auct_end_date = ds.child("auct_end_date").value.toString()
                            m.auct_end_time = ds.child("auct_end_time").value.toString()
                            m.url = ds.child("url").value.toString()
                            m.auct_id = ds.child("auct_id").value.toString()
                            auc_griditem.add(m)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                    adapter!!.update(auc_griditem!!)

                }
            }
        })
        sear.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search_fun(newText)
                return true
            }
        })
        return view
    }

    private fun search_fun(newText: String?) {
        val griditem = ArrayList<auct_model>()
        for (obj: auct_model in auc_griditem) {
            if (obj.item_name.toLowerCase().contains(newText!!.toLowerCase())) {
                griditem.add(obj)
            }
        }
        adapter?.update(griditem!!)
        linear = LinearLayoutManager(context)
        recyclView?.layoutManager = linear
        recyclView?.setHasFixedSize(true)
        adapter = auct_adapter(context, griditem!!)
        recyclView?.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}
/*
var d: String = ds.child("auct_end_date").value.toString()
var t: String = ds.child("auct_end_time").value.toString()
val calen = Calendar.getInstance()
val currentdate = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
val savecurrent = currentdate.format(calen.time)
val currenttime = SimpleDateFormat("hh:mm a", Locale.getDefault())
val savecurrenttime = currenttime.format(calen.time)
if (d.equals(savecurrent) && t.equals(savecurrenttime)) {
    continue
} else {

}
Log.d("data", ds.child("item_description").value.toString())*/

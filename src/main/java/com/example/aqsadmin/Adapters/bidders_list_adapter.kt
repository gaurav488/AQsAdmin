package com.example.aqsadmin.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aqsadmin.R
import com.example.aqsadmin.models.bidder_list_model
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class bidders_list_adapter  (var context: Context?, var arrayList: ArrayList<bidder_list_model>) :
    RecyclerView.Adapter<bidders_list_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.bidders_list_layout, parent, false)
        return ItemHolder(viewHolder)
    }
    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun update(list:ArrayList<bidder_list_model>){
        this.arrayList=list
        Collections.sort(arrayList,
            Comparator { o1, o2 ->
                o1.bid_amount.compareTo(o2.bid_amount)
            })
        arrayList.reverse()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder:bidders_list_adapter.ItemHolder, position: Int) {
        val GridItem: bidder_list_model= arrayList.get(position)
        holder.bidername.text = GridItem.username
        holder.bideramount.text=GridItem.bid_amount
        holder.time.text=GridItem.current_date_time
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bideramount = itemView.findViewById<TextView>(R.id.bideramount)
        var bidername= itemView.findViewById<TextView>(R.id.bidername)
        var time = itemView.findViewById<TextView>(R.id.time)
    }
}
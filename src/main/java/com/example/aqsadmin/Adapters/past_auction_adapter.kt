package com.example.aqsadmin.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.aqsadmin.R
import com.example.aqsadmin.models.past_auct_model
import com.example.aqsadmin.past_item_info

class past_auction_adapter (var context: Context?, var arrayList: ArrayList<past_auct_model>) :
    RecyclerView.Adapter<past_auction_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.past_auct_item_layout, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun update(list: ArrayList<past_auct_model>) {
        this.arrayList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: past_auction_adapter.ItemHolder, position: Int) {
        val GridItem: past_auct_model= arrayList.get(position)

        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(context!!).load(GridItem.url).dontAnimate().placeholder(circularProgressDrawable).into(holder.auct_img_id)
        holder.past_auct_item_name.text = GridItem.item_name
        holder.estimated_bid_amt.text = GridItem.item_amt+"-"+GridItem.estimated_end_bid
        holder.end_time.text = GridItem.auct_end_date+","+GridItem.auct_end_time
        holder.card.setOnClickListener {
            val intent = Intent(context, past_item_info::class.java)
            intent.putExtra("auct_id", GridItem.auct_id.toString())
            intent.putExtra("itemname", GridItem.item_name.toString())
            intent.putExtra("item_amt", GridItem.item_amt.toString())
            intent.putExtra("itemdes",GridItem.item_description.toString())
            it.context.startActivity(intent)
        }
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val auct_img_id=  itemView.findViewById<ImageView>(R.id.auct_img_id)
        val past_auct_item_name=itemView.findViewById<TextView>(R.id.past_auct_item_name)
        val estimated_bid_amt=itemView.findViewById<TextView>(R.id.estimated_bid_amt)
        val end_time = itemView.findViewById<TextView>(R.id.end_time)
        val card= itemView.findViewById<CardView>(R.id.past_auct_card)

    }
}
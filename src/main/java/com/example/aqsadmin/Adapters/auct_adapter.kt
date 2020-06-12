package com.example.aqsadmin.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aqsadmin.R
import com.example.aqsadmin.auction_item_details
import com.example.aqsadmin.edit_auct_item
import com.example.aqsadmin.models.auct_model
import com.google.firebase.database.FirebaseDatabase


class auct_adapter (var context: Context?, var arrayList: ArrayList<auct_model>) :
    RecyclerView.Adapter<auct_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.auct_items_layout, parent, false)
        return ItemHolder(viewHolder)
    }
    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun update(list:ArrayList<auct_model>){
        this.arrayList=list
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: auct_adapter.ItemHolder, position: Int) {
        val GridItem: auct_model = arrayList.get(position)
        val  dbRef = FirebaseDatabase.getInstance().getReference().child("Cart").child("Users")
        Glide.with(context!!).load(GridItem.url).dontAnimate().into(holder.active_icon )
        holder.active_name.text = "Name : "+GridItem.item_name
        holder.active_price.text="Price : "+GridItem.item_amt+"$"
        holder.active_description.text="Description : "+GridItem.item_description
        holder.active_card.setOnClickListener{
            val intent= Intent(context,auction_item_details::class.java)
            intent.putExtra("auct_id",GridItem.auct_id.toString())
            intent.putExtra("itemname",GridItem.item_name.toString())
            it.context.startActivity(intent)
        }
        holder.options.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().getReference().child("auctions")
            AlertDialog.Builder(context).apply {
                setTitle("Do you want to Edit or Remove?")
                setPositiveButton("Remove") { _, _ ->
                    dbRef.child(GridItem.auct_id).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Auction has been removed", Toast.LENGTH_SHORT)
                                .show()
                            arrayList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, arrayList.size)
                            update(arrayList)
                        }
                }
                setNeutralButton("Edit") { _, _ ->
                    val intent= Intent(context,edit_auct_item::class.java)
                    intent.putExtra("auct_id",GridItem.auct_id.toString())
                    context!!.startActivity(intent)
                }
                setNegativeButton("Cancel") { _, _ ->
                }.create().show()
            }
        }
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var active_icon = itemView.findViewById<ImageView>(R.id.active_auct_icon_img)
        var active_name= itemView.findViewById<TextView>(R.id.active_auct_name)
        var active_price = itemView.findViewById<TextView>(R.id.active_auct_price)
        var active_description=itemView.findViewById<TextView>(R.id.active_auct_des)
        var active_card = itemView.findViewById<CardView>(R.id.active_auct_card)
        var options=itemView.findViewById<ImageButton>(R.id.options)
    }
}
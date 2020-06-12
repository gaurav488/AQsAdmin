package com.example.aqsadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_auction_item_details.*
import kotlinx.android.synthetic.main.activity_past_item_info.*

class past_item_info : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_item_info)
        val auctid = getIntent().getExtras()?.get("auct_id").toString()
        val itemname = getIntent().getExtras()?.get("itemname").toString()

        val actionBar = supportActionBar
        actionBar?.title = itemname
        val auct_db_ref =
            FirebaseDatabase.getInstance().getReference().child("history").child(auctid)
        auct_db_ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                var prodname = p0.child("item_name").value.toString()
                var proddes = p0.child("item_description").value.toString()
                var url = p0.child("url").value.toString()
                var enddate = p0.child("auct_end_date").value.toString()
                var endtime = p0.child("auct_end_time").value.toString()
                var prodprice = p0.child("item_amt").value.toString()
                var estimated_end = p0.child("estimated_end_bid").value.toString()
                past_bid_end_date.setText("Ends on :" + enddate + "," + endtime)
                past_auct_item_name.setText("Name : " + prodname)
                past_estimated_amt.setText("Estimated Bid : " + prodprice+"-"+estimated_end)
                past_auct_item_des.setText("Description : " + proddes)
                Glide.with(applicationContext).load(url).dontAnimate()
                    .into(past_auct_desc_img)
            }
        })
      /*  val bidlistbtn=findViewById<Button>(R.id.bidders_list)
        bidlistbtn.setOnClickListener {
            val intent= Intent(applicationContext,com.example.aqsadmin.bidders_list::class.java)
            intent.putExtra("auct_id",auctid)
            startActivity(intent)
        }*/
    }
}



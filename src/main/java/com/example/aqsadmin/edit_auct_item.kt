package com.example.aqsadmin

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_admin_login.*
import kotlinx.android.synthetic.main.activity_create_auction.*
import kotlinx.android.synthetic.main.activity_edit_auct_item.*
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

class edit_auct_item : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageref: StorageReference? = null
    var c = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_auct_item)
        val auct_id= getIntent().getExtras()?.get("auct_id").toString()
        storage = FirebaseStorage.getInstance()
        storageref = storage!!.reference

        val btn = findViewById(R.id.edit_btnchoose) as Button
        btn.setOnClickListener{
            showfileChooser()
        }
        val ref = FirebaseDatabase.getInstance().reference.child("auction").child(auct_id)
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(ds: DataSnapshot) {
                if (ds.exists()) {
                    if (ds.hasChild("url")) {
                        val imgurl = ds.child("url").value.toString()
                        val edit_img=findViewById(R.id.edit_imageView)  as ImageView
                        Glide.with(this@edit_auct_item).load(imgurl).into(edit_img)
                    }
                    if (ds.hasChild("item_name")) {
                        val pname= ds.child("item_name").value.toString()
                        edit_item_name.setText(pname)
                    }
                    if (ds.hasChild("item_description")) {
                        val pdes = ds.child("item_description").value.toString()
                        edit_item_desc.setText(pdes)
                    }
                    if (ds.hasChild("item_amt")) {
                        var pprice = ds.child("item_amt").value.toString()
                        edit_item_strt_amt.setText(pprice)
                    }
                    if (ds.hasChild("estimated_end_bid")) {
                        var estimate_end_bid = ds.child("estimated_end_bid").value.toString()
                        edit_item_estimate_end_amt.setText(estimate_end_bid)
                    }
                    if (ds.hasChild("auct_end_date")) {
                        var previous_date= ds.child("auct_end_date").value.toString()
                        edit_Date.setText(previous_date)
                    }
                    if (ds.hasChild("auct_end_time")) {
                        var previous_time= ds.child("auct_end_time").value.toString()
                        edit_pick_end_time.setText(previous_time)
                    }
                    if(ds.hasChild("estimated_end")){
                        var estimatedend= ds.child("estimated_end").value.toString()
                        edit_item_estimate_end_amt.setText(estimatedend)
                    }
                    if(ds.hasChild("estimated_start")){
                        var estimatedstart= ds.child("estimated_start").value.toString()
                        edit_item_estimate_strt_amt.setText(estimatedstart)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })



        val datetime= findViewById<Button>(R.id.edit_pick_date)
        datetime.setOnClickListener {

            var timeformat= SimpleDateFormat("dd MMM, yyyy",Locale.getDefault())
            val datepicker = DatePickerDialog(this@edit_auct_item,android.R.style.Theme_Holo_Light_Dialog,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    c.set(Calendar.YEAR,year)
                    c.set(Calendar.MONTH,month)
                    c.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                    val selectformats= timeformat.format(c.time)
                    edit_Date.text = selectformats
                },
                c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH))
            datepicker.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            datepicker.show()
        }
        val pick_time= findViewById<Button>(R.id.edit_pick_time)
        pick_time.setOnClickListener {
            var timeformat= SimpleDateFormat("hh:mm a")
            val selectedtime=Calendar.getInstance()
            val timepicker= TimePickerDialog(this@edit_auct_item,android.R.style.Theme_Holo_Light_Dialog,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    selectedtime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                    selectedtime.set(Calendar.MINUTE,minute)
                    val time = timeformat.format(selectedtime.time)
                    edit_pick_end_time.text = time
                },
                c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),false)
            timepicker.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            timepicker.show()
        }
        val edit_save= findViewById(R.id.edit_creatauc) as Button
       edit_save.setOnClickListener{
            uploadFile()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, filePath)
                val imgv = findViewById(R.id.edit_imageView) as ImageView
                imgv.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    private fun uploadFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this@edit_auct_item)


            val imageref = storageref!!.child("image/" + UUID.randomUUID().toString())
            imageref.putFile(filePath!!)
                .addOnCompleteListener {
                    Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_SHORT).show()
                    auct_detail(it)
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun showfileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST
        )

    }
    private fun auct_detail(it: Task<UploadTask.TaskSnapshot>) {

        val itemn = edit_item_name.text.toString().trim()
        val itemdes = edit_item_desc.text.toString().trim()
        val itemdate= edit_Date.text.toString().trim()
        val itemamt =edit_item_strt_amt.text.toString().trim()
        val enddate=edit_pick_end_time.text.toString().trim()
        val estimate_end=edit_item_estimate_end_amt.text.toString().trim()
        val estimate_start=edit_item_estimate_strt_amt.text.toString().trim()
        val auctId: String = UUID.randomUUID().toString() //random id generated
        val hashMap = HashMap<String, Any>()
        if (itemn.isNotEmpty()) {
            hashMap["item_name"] = itemn
        }
        if (itemdes.isNotEmpty()) {
            hashMap["item_description"] = itemdes
        }
        if (itemamt.isNotEmpty()) {
            hashMap["item_amt"] = itemamt
        }
        if (itemdate.isNotEmpty()) {
            hashMap["auct_end_date"] = itemdate
        }
        if (enddate.isNotEmpty()) {
            hashMap["auct_end_time"] = enddate
        }
        if (estimate_end.isNotEmpty()) {
            hashMap["estimated_end_bid"] = estimate_end
        }
        if (estimate_start.isNotEmpty()) {
            hashMap["estimated_start"] = estimate_start
        }

        if (it != null) {
            try {
                it.result?.storage?.downloadUrl?.addOnSuccessListener {
                    hashMap["url"] = it.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val ref = FirebaseDatabase.getInstance().reference
            ref.child("auction").child(auctId).updateChildren(hashMap).addOnCompleteListener {
                Toast.makeText(applicationContext, " Saved successfully ", Toast.LENGTH_LONG).show()
            }
        }
        startActivity(Intent(this,auction_item_details::class.java))


    }
}

/*
if(it!=null)
{
    try {
        it.result!!.storage.downloadUrl.addOnSuccessListener {
            val itemn = item_name.text.toString().trim()
            val itemdes = item_desc.text.toString().trim()
            val itemdate= Date.text.toString().trim()
            val itemamt =item_strt_amt.text.toString().trim()
            val enddate=pick_end_time.text.toString().trim()
            val estimate_start=item_estimate_strt_amt.text.toString().trim()
            val estimate_end=item_estimate_end_amt.text.toString().trim()
            val uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
            val auctId: String = UUID.randomUUID().toString() //random id generated
            val hashMap: HashMap<String, String> = HashMap<String,String>()
            hashMap.put("item_name",itemn)
            hashMap.put("item_description",itemdes)
            hashMap.put("item_amt",itemamt)
            hashMap.put("Uid",uid)
            hashMap.put("estimated_",estimate_start+"-"+estimate_end)
            hashMap.put("auct_end_date",itemdate)
            hashMap.put("auct_end_time",enddate)
            hashMap.put("auct_id",auctId)
            hashMap.put("url",it.toString())
            val ref = FirebaseDatabase.getInstance().reference
            ref.child("auction").child(auctId).setValue(hashMap).addOnCompleteListener {
                Toast.makeText(applicationContext, " Saved successfully ", Toast.LENGTH_LONG).show()
            }
        }
    }catch (e: Exception){
        e.printStackTrace()
    }
}*/

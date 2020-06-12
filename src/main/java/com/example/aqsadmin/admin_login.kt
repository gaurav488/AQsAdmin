package com.example.aqsadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_admin_login.*

class admin_login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
        auth = FirebaseAuth.getInstance()
        Lgin.setOnClickListener() {
            lgin()
        }
    }
    private fun lgin() {
        val emai = findViewById<View>(R.id.Email) as EditText
        val pa1 = findViewById<View>(R.id.Password) as EditText
        val em2 = emai.text.toString()
        val ps2 = pa1.text.toString()
        FirebaseDatabase.getInstance().getReference("Admin").child("gD0xTbIlWIRkdLT3A6rqaqNWDbm1").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.hasChild("email")){
                    var email = p0.child("email").value.toString()
                    if (em2.isEmpty() || ps2.isEmpty()) {
                        Toast.makeText(applicationContext, "Enter Required Credentials", Toast.LENGTH_LONG).show()
                    }
                    else {
                        auth.signInWithEmailAndPassword(em2, ps2)
                            .addOnCompleteListener{
                                if(em2!=email){
                                    Toast.makeText(applicationContext, "Email is incorrect", Toast.LENGTH_LONG).show()
                                }
                                else{
                                    if (it.isSuccessful) {
                                        Toast.makeText(applicationContext, "Welcome Admin", Toast.LENGTH_LONG
                                        ).show()
                                        startActivity(Intent(applicationContext, MainActivity::class.java))
                                    }
                                    else{
                                        Toast.makeText(applicationContext,"Failed to login", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                    }
                }
            }

        })

    }


    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

package com.example.aqsadmin


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_manage_auction.*

/**
 * A simple [Fragment] subclass.
 */
class manage_auction : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_auction, container, false)
        val create_auct=view.findViewById<CardView>(R.id.create_auction_card)
        val past_auct=view.findViewById<CardView>(R.id.past_auction_card)
        create_auct.setOnClickListener {
            val intent= Intent(context,create_auction::class.java)
            startActivity(intent)
        }
       past_auct.setOnClickListener {
            val intent= Intent(context,past_auction::class.java)
            startActivity(intent)
        }
        val logoutcard=view.findViewById(R.id.admin_log_out_card) as CardView

        logoutcard.setOnClickListener {
            android.app.AlertDialog.Builder(context).apply {
                setTitle("Are you Sure you want to Logout?")
                setPositiveButton("OK") { _, _ ->
                    LogOut()
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton("Cancel") { _, _ ->
                }
            }.create().show()
        }
        return view
    }
    private fun LogOut() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(context, "Logged Out!", Toast.LENGTH_LONG).show()
        startActivity(Intent(context,admin_login::class.java))
    }


}

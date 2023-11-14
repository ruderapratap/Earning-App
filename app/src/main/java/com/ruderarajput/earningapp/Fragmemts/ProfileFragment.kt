package com.ruderarajput.earningapp.Fragmemts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.ruderarajput.earningapp.Activity.HomeActivity
import com.ruderarajput.earningapp.MainActivity
import com.ruderarajput.earningapp.Models.User
import com.ruderarajput.earningapp.R
import com.ruderarajput.earningapp.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

  val binding by lazy {
    FragmentProfileBinding.inflate(layoutInflater)
  }
    var isExpend=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        
        binding.btnUpdown.setOnClickListener {
            if (isExpend) {
                binding.linearlayout.visibility = View.VISIBLE
                binding.btnUpdown.setImageResource(R.drawable.down_arrow)
            } else {
                binding.linearlayout.visibility = View.GONE
                binding.btnUpdown.setImageResource(R.drawable.up_arrow)
            }
            isExpend = !isExpend
        }
        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid).addListenerForSingleValueEvent(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   for (datasnap in snapshot.children){
                       var user=snapshot.getValue<User>()

                       binding.userName.text=user?.name
                       binding.userAge.text=user?.age.toString()
                       binding.userEmail.text=user?.email
                       binding.userPassword.text=user?.password
                      binding.nameProfile.text=user?.name

                   }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
        binding.btnLogout.setOnClickListener {
            val builder= AlertDialog.Builder(requireContext())
            builder.setTitle("Log Out")
            builder.setMessage("Do you want to logout this application.")
           // builder.setIcon(android.R.drawable.sym_def_app_icon)
            builder.setPositiveButton("Yes"){dialogInterface,which->
                val intent=Intent(requireContext(),MainActivity::class.java)
                Firebase.auth.signOut()
                startActivity(intent)
                requireActivity().finish()
            }
            builder.setNegativeButton("No"){dialogInterface,which->
                val intent=Intent(requireContext(),HomeActivity::class.java)
                startActivity(intent)
            }
            builder.setNeutralButton("Cancel"){dialogInterface,which->
               builder.setCancelable(true)
            }
            val dialog=builder.create()
            dialog.setCancelable(false)
            dialog.show()
        }
        return binding.root
    }

}
package com.ruderarajput.earningapp.Fragmemts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.ruderarajput.earningapp.Adapters.CategoryAdapter
import com.ruderarajput.earningapp.Models.User
import com.ruderarajput.earningapp.Models.categoryModelClass
import com.ruderarajput.earningapp.R
import com.ruderarajput.earningapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private val binding:FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private var categoryList=ArrayList<categoryModelClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryList.add(categoryModelClass(R.drawable.android_ic,"Android"))
        categoryList.add(categoryModelClass(R.drawable.kotlin_ic,"Kotlin"))
        categoryList.add(categoryModelClass(R.drawable.swift_ic,"Swift"))
        categoryList.add(categoryModelClass(R.drawable.java_ic,"Java"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.withdrawalCoin.setOnClickListener {
            val bottomSheetDialog:BottomSheetDialogFragment=WithdrawalFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }
        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (datasnap in snapshot.children){
                        var user=snapshot.getValue<User>()

                        binding.userName.text=user?.name
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var currentCoin=snapshot.getValue() as Long
                        binding.coinCount.text=currentCoin.toString()

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recView.layoutManager=GridLayoutManager(requireContext(),2)
        var adapter=CategoryAdapter(categoryList,requireActivity())
        binding.recView.adapter=adapter
        binding.recView.setHasFixedSize(true)
    }
}
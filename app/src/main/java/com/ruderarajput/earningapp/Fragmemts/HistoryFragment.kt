package com.ruderarajput.earningapp.Fragmemts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.ruderarajput.earningapp.Adapters.HistoryAdapter
import com.ruderarajput.earningapp.Models.HistoryModelClass
import com.ruderarajput.earningapp.Models.User
import com.ruderarajput.earningapp.R
import com.ruderarajput.earningapp.databinding.FragmentHistoryBinding
import com.ruderarajput.earningapp.databinding.FragmentHomeBinding
import java.util.Collections


class HistoryFragment : Fragment() {

    val binding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }
    lateinit var adapter:HistoryAdapter
    private var ListHistory=ArrayList<HistoryModelClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    ListHistory.clear()
                    var ListHistory1=ArrayList<HistoryModelClass>()
                    for (datasnapshot in snapshot.children){
                        var data=datasnapshot.getValue(HistoryModelClass::class.java)
                        ListHistory1.add(data!!)
                    }
                    Collections.reverse(ListHistory1)
                    ListHistory.addAll(ListHistory1)
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                   // TODO("Not yet implemented")
                }

            })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.withdrawalCoin.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment =WithdrawalFragment()
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

        binding.recViewHistory.layoutManager=LinearLayoutManager(requireContext())
        adapter=HistoryAdapter(ListHistory)
        binding.recViewHistory.adapter=adapter
        binding.recViewHistory.setHasFixedSize(true)
        return binding.root
    }
}
package com.ruderarajput.earningapp.Fragmemts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.ruderarajput.earningapp.Models.HistoryModelClass
import com.ruderarajput.earningapp.Models.User
import com.ruderarajput.earningapp.R
import com.ruderarajput.earningapp.databinding.FragmentWithdrawalBinding


class WithdrawalFragment : BottomSheetDialogFragment() {

    private val binding by lazy {
        FragmentWithdrawalBinding.inflate(layoutInflater)
    }
    var currentCoin=0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var currentCoin=snapshot.getValue() as Long
                        binding.btnMoneyTransfer.setOnClickListener {
                            if (binding.etAmount.text.toString().toDouble()<=currentCoin){
                                Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).setValue(currentCoin-binding.etAmount.text.toString().toDouble())

                                var historyModelClass= HistoryModelClass(System.currentTimeMillis().toString(),binding.etAmount.text.toString(),true)
                                Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid).push().setValue(historyModelClass)
                                Toast.makeText(requireContext(),"Succesfully Money Transfer",Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(requireContext(),"Out of money",Toast.LENGTH_SHORT).show()
                            }
                        }
                        binding.etPaytmNumber.text!!.clear()
                        binding.etAmount.text!!.clear()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var currentCoin=snapshot.getValue() as Long
                        binding.totalCoin.text=currentCoin.toString()

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return binding.root
    }

}
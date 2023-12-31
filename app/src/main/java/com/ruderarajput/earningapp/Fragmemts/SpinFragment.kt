package com.ruderarajput.earningapp.Fragmemts

import android.os.Bundle
import android.os.CountDownTimer
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
import com.ruderarajput.earningapp.databinding.FragmentSpinBinding
import java.util.Objects
import kotlin.random.Random


class SpinFragment : Fragment() {
    private lateinit var binding: FragmentSpinBinding
    private lateinit var timer: CountDownTimer
    private val itemTitles= arrayOf("100","Try Again","500","Try Again","200","Try Again")
    var currentChance=0L
    var currentCoin=0L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentSpinBinding.inflate(inflater, container, false)

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
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        currentChance=snapshot.getValue() as Long
                      binding.chance.text=(snapshot.getValue() as Long).toString()
                    }else{
                        var temp=0
                        binding.chance.text=temp.toString()
                        binding.spinBtn.isEnabled=false
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
                        currentCoin=snapshot.getValue() as Long
                        binding.coinCount.text=currentCoin.toString()

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return binding.root
    }
    private fun showResult(itemTitle:String,spin:Int){
        if (spin%2==0){
            var winCoin=itemTitle.toInt()
            Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).setValue(winCoin+currentCoin)
            binding.coinCount.text= (winCoin+currentCoin).toString()
            var historyModelClass=HistoryModelClass(System.currentTimeMillis().toString(),winCoin.toString(),false)
            Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid).push().setValue(historyModelClass)
        }
        Toast.makeText(requireContext(),itemTitle,Toast.LENGTH_SHORT).show()
        currentChance=currentChance-1
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).setValue(currentChance)
        binding.spinBtn.isEnabled=true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spinBtn.setOnClickListener {
            binding.spinBtn.isEnabled=false
            if (currentChance>0){
                val spin= Random.nextInt(6)
                val degrees=60f*spin


                timer=object :CountDownTimer(5000,50){
                    var rotation=0f

                    override fun onTick(p0: Long) {
                        rotation += 10f
                        if (rotation>=degrees){
                            rotation=degrees
                            timer.cancel()
                            showResult(itemTitles[spin],spin)
                        }
                        binding.spinnerWheel.rotation=rotation
                    }

                    override fun onFinish() {}
                }.start()
            }else{
                Toast.makeText(requireContext(),"out of spin chance.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package com.ruderarajput.earningapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ruderarajput.earningapp.Fragmemts.WithdrawalFragment
import com.ruderarajput.earningapp.Models.Question
import com.ruderarajput.earningapp.Models.User
import com.ruderarajput.earningapp.R
import com.ruderarajput.earningapp.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    var currentQuestion=0
    var score=0
    var currentChance=0L
    private lateinit var questionsList:ArrayList<Question>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        currentChance=snapshot.value as Long
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


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

        questionsList=ArrayList<Question>()
        var catText=intent.getStringExtra("questionsType")
        var image=intent.getIntExtra("categoryImage",0)
        binding.categoryImage.setImageResource(image)

        Firebase.firestore.collection("Questions").document(catText.toString()).collection("question1")
            .get().addOnSuccessListener {
                    questionData->
                questionsList.clear()
                for (data in questionData.documents){
                    var qusetion:Question?=data.toObject(Question::class.java)
                    questionsList.add(qusetion!!)
                }
                if (questionsList.size >0){
                    binding.question.text=questionsList.get(currentQuestion).question
                    binding.option1.text=questionsList.get(currentQuestion).option1
                    binding.option2.text=questionsList.get(currentQuestion).option2
                    binding.option3.text=questionsList.get(currentQuestion).option3
                    binding.option4.text=questionsList.get(currentQuestion).option4
                }

            }
        binding.withdrawalCoin.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = WithdrawalFragment()
            bottomSheetDialog.show(this@QuizActivity.supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }
        binding.option1.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option1.text.toString())
        }
        binding.option2.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option2.text.toString())
        }
        binding.option3.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option3.text.toString())
        }
        binding.option4.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option4.text.toString())
        }
    }
    private fun nextQuestionAndScoreUpdate(s:String) {
        if (s.equals(questionsList.get(currentQuestion).ans)){
            score+=10
            //Toast.makeText(this,score.toString(),Toast.LENGTH_SHORT).show()
        }//(score/(questionsList.size*10))*100
        currentQuestion++
        if (currentQuestion >= questionsList.size) {
           if (score>=30){
               binding.winnerLottie.visibility=View.VISIBLE
               var isUpdated=false
               if (isUpdated){

               }else{
                               Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).setValue(currentChance+1)
                                   .addOnSuccessListener {
                                       isUpdated=true
                                   }
               }
           }else{
               binding.sorryLottie.visibility=View.VISIBLE
           }
        } else {
            binding.question.text = questionsList.get(currentQuestion).question
            binding.option1.text = questionsList.get(currentQuestion).option1
            binding.option2.text = questionsList.get(currentQuestion).option2
            binding.option3.text = questionsList.get(currentQuestion).option3
            binding.option4.text = questionsList.get(currentQuestion).option4
        }
    }
}
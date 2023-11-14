package com.ruderarajput.earningapp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ruderarajput.earningapp.Activity.HomeActivity
import com.ruderarajput.earningapp.Models.User
import com.ruderarajput.earningapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var progressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        progressDialog = Dialog(this)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.prograssbar)
        progressDialog.setCancelable(false)


        simulateTask()

        binding.signupBtn.setOnClickListener {
           // progressDialog.show()
            if (binding.etName.text.toString().equals("")||
                binding.etAge.text.toString().equals("")||
                binding.etEmail.text.toString().equals("")||
                binding.etPassword.text.toString().equals(""))
            {
                progressDialog.dismiss()
                Toast.makeText(this,"please fill all the details",Toast.LENGTH_SHORT).show()
            }else
            {
                Firebase.auth.createUserWithEmailAndPassword(binding.etEmail.text.toString(),binding.etPassword.text.toString())
                    .addOnCompleteListener {task->
                        if (task.isSuccessful){
                            val user= User(binding.etName.text.toString(), binding.etAge.text.toString().toInt(),binding.etEmail.text.toString(),binding.etPassword.text.toString())
                            Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid).setValue(user).addOnSuccessListener {
                                Toast.makeText(this, "Sign Up Successfully", Toast.LENGTH_SHORT).show()
                                val intent=Intent(this@MainActivity,HomeActivity::class.java)
                                startActivity(intent)
                                 progressDialog.dismiss()
                                finish()
                            }


                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(this,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun simulateTask() {
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
        }, 3000)
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser!=null){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }
}
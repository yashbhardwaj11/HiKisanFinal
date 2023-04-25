package com.devinfusion.hikisanfinal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.devinfusion.hikisanfinal.MainActivity
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var number : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        binding.signInBt.setOnClickListener {
            number = binding.mobileEt.text.toString().trim()
            if (number.length < 10){
                Toast.makeText(this@LoginActivity, "Enter a valid number", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.progressBar.visibility = View.VISIBLE
                binding.signInBt.visibility = View.INVISIBLE
                number = "+91$number"
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(number)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }

    }

    fun init(){
        auth = FirebaseAuth.getInstance()
        val edit : EditText = findViewById(R.id.mobileEt)
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (s.length == 10){
                    binding.signInBt.setBackgroundColor(resources.getColor(R.color.main))
                    binding.signInBt.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(this@LoginActivity, "Error : ${p0.message}", Toast.LENGTH_SHORT).show()
            Log.d("ErrorAgaya",p0.message.toString() )
            Log.d("ErrorAgaya",number)
            binding.progressBar.visibility = View.INVISIBLE
            binding.signInBt.visibility = View.VISIBLE
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            val intent = Intent(this@LoginActivity,OtpActivity::class.java)
            intent.putExtra("OTP",verificationId)
            intent.putExtra("resendToken",token)
            intent.putExtra("phoneNumber",number)
            startActivity(intent)
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateUI(task.result.user)
                } else {
                    Toast.makeText(this@LoginActivity, "Signin Failed try again", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.signInBt.visibility = View.VISIBLE
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user!=null){
            binding.progressBar.visibility = View.VISIBLE
            binding.signInBt.visibility = View.INVISIBLE
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            finish()
        }
    }


    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }
}
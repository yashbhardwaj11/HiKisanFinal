package com.devinfusion.hikisanfinal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.devinfusion.hikisanfinal.MainActivity
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.dao.UserDao
import com.devinfusion.hikisanfinal.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOtpBinding
    private lateinit var auth : FirebaseAuth


    private lateinit var number : String
    private lateinit var OTP : String
    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inti()

        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        number = intent.getStringExtra("phoneNumber").toString()

        resendOtpVisiblity()

        binding.resendOtpTT.setOnClickListener {
            resendVerificationCode()
            resendOtpVisiblity()
        }

        binding.verifyOtp.setOnClickListener {
            val otp : String = binding.otpEt.text.toString().trim()
            if (otp.isNotEmpty()){
                if (otp.length<6){
                    Toast.makeText(this@OtpActivity, "Enter valid otp", Toast.LENGTH_SHORT).show()
                }
                else{
                    binding.progressBar.visibility = View.VISIBLE
                    binding.verifyOtp.visibility = View.INVISIBLE
                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(OTP,otp)
                    signInWithPhoneAuthCredential(credential)
                }
            }
            else{
                Toast.makeText(this@OtpActivity, "Field should not be empty", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun inti(){
        auth = FirebaseAuth.getInstance()
    }

    private fun resendOtpVisiblity(){
        binding.otpEt.setText("")
        binding.resendOtpTT.visibility = View.INVISIBLE
        binding.resendOtpTT.isEnabled = false
        Handler(Looper.myLooper()!!).postDelayed({
            binding.resendOtpTT.visibility = View.VISIBLE
            binding.resendOtpTT.isEnabled = true
        },60000)
    }

    private fun resendVerificationCode(){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(this@OtpActivity, "Error : ${p0.message}", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.INVISIBLE
            binding.verifyOtp.visibility = View.VISIBLE
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            OTP = verificationId
            resendToken = token
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateUI(task.result.user)
                } else {
                    Toast.makeText(this@OtpActivity, "SignIn Failed try again", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.verifyOtp.visibility = View.VISIBLE
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user!=null){
            binding.progressBar.visibility = View.VISIBLE
            binding.verifyOtp.visibility = View.INVISIBLE
            checkUserExists(user)
        }
    }

    private fun checkUserExists(user: FirebaseUser?) {
        val userDao = UserDao()
        userDao.userCollection.child(user!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    finish()
                }else{
                    val i = Intent(applicationContext,ProfileActivity::class.java)
                    i.putExtra("uid",user.uid)
                    startActivity(i)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
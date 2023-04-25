package com.devinfusion.hikisanfinal.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devinfusion.hikisanfinal.model.Kisan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainViewModel : ViewModel() {
    private val kisanLiveData = MutableLiveData<Kisan>()
    private val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("User")
        .child(FirebaseAuth.getInstance().currentUser!!.uid)

    val kisan: MutableLiveData<Kisan> get() = kisanLiveData

    init {
        getData()
    }

    public fun getUser() : MutableLiveData<Kisan>{
        return kisanLiveData
    }

    private fun getData() {
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(Kisan::class.java)
                    kisanLiveData.value = user!!
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled() event
            }
        })
    }


}

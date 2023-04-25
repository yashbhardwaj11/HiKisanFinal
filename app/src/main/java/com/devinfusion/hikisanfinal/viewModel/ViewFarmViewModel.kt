package com.devinfusion.hikisanfinal.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devinfusion.hikisanfinal.model.FarmSell
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewFarmViewModel : ViewModel() {


    private val farmOnSale = MutableLiveData<FarmSell>()
    private val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("Farm")
        .child(FirebaseAuth.getInstance().currentUser!!.uid)

    val farmSale: MutableLiveData<FarmSell> get() = farmOnSale

    init {
        getData()
    }

    public fun getUser() : MutableLiveData<FarmSell> {
        return farmSale
    }

    private fun getData() {
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(FarmSell::class.java)
                    farmOnSale.value = user!!
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled() event
            }
        })
    }

    init {
        getData()
    }

}
package com.devinfusion.hikisanfinal.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devinfusion.hikisanfinal.model.Buyer
import com.devinfusion.hikisanfinal.model.FarmSell
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BuyerViewModel : ViewModel() {
    private var buyerList = MutableLiveData<List<Buyer>>()

    init {
        buyerList = MutableLiveData()
        getData()
    }

    public fun getFarmList() : MutableLiveData<List<Buyer>> {
        return buyerList
    }

    private fun getData(){
        val dbRef = FirebaseDatabase.getInstance().getReference("Buyer")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val farms : ArrayList<Buyer> = arrayListOf()
                    for (data in snapshot.children){
                        val farm : Buyer = data.getValue(Buyer::class.java)!!
                        farms.add(farm)
                    }
                    buyerList.postValue(farms)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}
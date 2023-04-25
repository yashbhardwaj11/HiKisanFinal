package com.devinfusion.hikisanfinal.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devinfusion.hikisanfinal.model.FarmSell
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FarmViewModel : ViewModel() {
    private var farmList = MutableLiveData<List<FarmSell>>()

    init {
        farmList = MutableLiveData()
        getData()
    }

    public fun getFarmList() : MutableLiveData<List<FarmSell>> {
        return farmList
    }

    private fun getData(){
        val dbRef = FirebaseDatabase.getInstance().getReference("Farm")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val farms : ArrayList<FarmSell> = arrayListOf()
                    for (data in snapshot.children){
                        val farm : FarmSell = data.getValue(FarmSell::class.java)!!
                        farms.add(farm)
                    }
                    farmList.postValue(farms)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}

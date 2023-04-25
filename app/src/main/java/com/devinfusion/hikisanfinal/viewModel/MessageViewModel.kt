package com.devinfusion.hikisanfinal.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devinfusion.hikisanfinal.model.FarmSell
import com.devinfusion.hikisanfinal.model.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageViewModel : ViewModel() {
    private var messageList = MutableLiveData<List<Message>>()

    init {
        messageList = MutableLiveData()
        getData()
    }

    public fun getFarmList() : MutableLiveData<List<Message>> {
        return messageList
    }

    private fun getData(){
        val dbRef = FirebaseDatabase.getInstance().getReference("Chats")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val farms : ArrayList<Message> = arrayListOf()
                    for (data in snapshot.children){
                        val farm : Message = data.getValue(Message::class.java)!!
                        farms.add(farm)
                    }
                    messageList.postValue(farms)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}
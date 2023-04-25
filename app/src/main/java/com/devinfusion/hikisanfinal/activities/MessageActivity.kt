package com.devinfusion.hikisanfinal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.adapter.MessageAdapter
import com.devinfusion.hikisanfinal.dao.UserDao
import com.devinfusion.hikisanfinal.databinding.ActivityMessageBinding
import com.devinfusion.hikisanfinal.model.Kisan
import com.devinfusion.hikisanfinal.model.Message
import com.devinfusion.hikisanfinal.viewModel.MessageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMessageBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList : ArrayList<Message>
    private lateinit var mDbref : DatabaseReference
    private lateinit var adapter: MessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDbref = FirebaseDatabase.getInstance().getReference()
        adapter = MessageAdapter(this)
        binding.messageRV.adapter =adapter
        binding.messageRV.layoutManager = LinearLayoutManager(this)

        val messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        val messageList : LiveData<List<Message>> = messageViewModel.getFarmList()

        messageList.observe(this){
            adapter.setList(it)
        }


        binding.sendMessageBT.setOnClickListener {
            val message = binding.messageEt.text.toString()
            val messageObject = Message(message,FirebaseAuth.getInstance().currentUser!!.uid)
            if (message.isNotEmpty()){
                mDbref.child("Chats").push().setValue(messageObject)
                    .addOnSuccessListener {
                        binding.messageEt.setText("")
                    }
            }
        }
    }

}
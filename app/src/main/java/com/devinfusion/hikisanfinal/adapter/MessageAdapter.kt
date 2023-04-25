package com.devinfusion.hikisanfinal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.model.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context : Context) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messageList  :List<Message> = listOf()

    public fun setList(mlist : List<Message>){
        messageList = mlist
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val sentMessage : TextView = view.findViewById(R.id.txt_send_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sent_layout,parent,false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val current = messageList[position]
        holder.sentMessage.text = current.message
    }
}
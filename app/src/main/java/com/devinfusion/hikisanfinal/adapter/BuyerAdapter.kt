package com.devinfusion.hikisanfinal.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.activities.BuyerFullViewActivity
import com.devinfusion.hikisanfinal.fragment.BuyerFullViewFragment
import com.devinfusion.hikisanfinal.model.Buyer

class BuyerAdapter(val context : Context) : RecyclerView.Adapter<BuyerAdapter.BuyerViewHolder>() {

    private var mlist : List<Buyer> = listOf()

    public fun setList(buyerList : List<Buyer>){
        mlist = buyerList
        notifyDataSetChanged()
    }

    inner class BuyerViewHolder(view : View) : ViewHolder(view){
        val companyIv : ImageView = view.findViewById(R.id.buyerIv)
        val companyName : TextView = view.findViewById(R.id.companyName)
        val title : TextView = view.findViewById(R.id.companyTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.buyer_item,parent,false)
        return BuyerViewHolder(view)
    }

    override fun getItemCount(): Int = mlist.size

    override fun onBindViewHolder(holder: BuyerViewHolder, position: Int) {
        val current = mlist[position]
        holder.companyIv.load(current.companyPhoto)
        holder.companyName.text = current.companyName
        holder.title.text = current.title
        holder.itemView.setOnClickListener {
            val intent  = Intent(context,BuyerFullViewActivity::class.java)
            intent.putExtra("companyName",current.companyName)
            intent.putExtra("companyPhoto",current.companyPhoto)
            intent.putExtra("title",current.title)
            intent.putExtra("desc",current.description)
            intent.putExtra("price",current.price)


            context.startActivity(intent)
        }
    }
}
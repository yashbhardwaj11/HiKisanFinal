package com.devinfusion.hikisanfinal.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.model.FarmSell

class FarmAdapter(val context : Context) : RecyclerView.Adapter<FarmAdapter.farmViewHolder>() {

    private var mlist : List<FarmSell> = listOf()

    public fun setList(farmlist : List<FarmSell>){
        mlist = farmlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): farmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.farm_item,parent,false)

        return farmViewHolder(view)
    }

    override fun getItemCount(): Int = mlist.size

    override fun onBindViewHolder(holder: farmViewHolder, position: Int) {
        val current = mlist[position]
        holder.farmPrice.text = current.farmPrice
        holder.farmSize.text = current.farmSize
        holder.farmerLocation.text = current.farmLocation
        holder.farmerName.text = current.farmerName
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("farmId",current.farmId)
            Navigation.findNavController(it).navigate(R.id.action_farmingFragment_to_viewFarmFragment,bundle)
        }
    }

    inner class farmViewHolder(view : View) : ViewHolder(view){
        val farmerName : TextView = view.findViewById(R.id.fameName)
        val farmerLocation : TextView = view.findViewById(R.id.farmLocation)
        val farmSize : TextView = view.findViewById(R.id.farmPrice)
        val farmPrice : TextView = view.findViewById(R.id.farmPrice)
    }
}
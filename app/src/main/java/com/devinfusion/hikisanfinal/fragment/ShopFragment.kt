package com.devinfusion.hikisanfinal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.adapter.BuyerAdapter
import com.devinfusion.hikisanfinal.adapter.FarmAdapter
import com.devinfusion.hikisanfinal.databinding.FragmentShopBinding
import com.devinfusion.hikisanfinal.model.Buyer
import com.devinfusion.hikisanfinal.model.FarmSell
import com.devinfusion.hikisanfinal.viewModel.BuyerViewModel
import com.devinfusion.hikisanfinal.viewModel.FarmViewModel
import com.google.firebase.database.FirebaseDatabase

class ShopFragment : Fragment() {
    private var _binding : FragmentShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : BuyerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(layoutInflater,container,false)

        binding.shopRv.layoutManager = LinearLayoutManager(requireContext())

        if (isAdded){
            adapter = BuyerAdapter(requireContext())
            binding.shopRv.adapter = adapter
        }

        val buyerViewModel : BuyerViewModel = ViewModelProvider(this).get(BuyerViewModel::class.java)
        val farmList : LiveData<List<Buyer>> = buyerViewModel.getFarmList()
        farmList.observe(viewLifecycleOwner){
            adapter.setList(it)
        }


        val ref = FirebaseDatabase.getInstance().getReference("Buyer")
        val key = ref.push().key
//        ref.child(key.toString()).setValue(Buyer(ref.key.toString(),"Mass Tender",
//            "BlinkIt is on for a bulk buying of products like fruits and vegetable interested ones can rsvp and will get the mail regarding the same",
//       "4000000","BlinkIt" ,"https://hindubabynames.info/downloads/wp-content/themes/hbn_download/download/logistics-companies/blinkit-logo.png",
//            key
//        ))


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
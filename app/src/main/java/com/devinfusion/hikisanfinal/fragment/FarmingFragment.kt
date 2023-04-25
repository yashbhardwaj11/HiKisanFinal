package com.devinfusion.hikisanfinal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.devinfusion.hikisanfinal.adapter.FarmAdapter
import com.devinfusion.hikisanfinal.viewModel.FarmViewModel
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.databinding.FragmentFarmingBinding
import com.devinfusion.hikisanfinal.model.FarmSell

class FarmingFragment : Fragment() {

    private var _binding : FragmentFarmingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FarmAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFarmingBinding.inflate(layoutInflater,container,false)

        binding.farmRv.layoutManager = LinearLayoutManager(requireContext())

        if (isAdded){
            adapter = FarmAdapter(requireContext())
            binding.farmRv.adapter = adapter
        }

        val farmViewModel : FarmViewModel = ViewModelProvider(this).get(FarmViewModel::class.java)
        val farmList : LiveData<List<FarmSell>> = farmViewModel.getFarmList()
        farmList.observe(viewLifecycleOwner){
            adapter.setList(it)
        }

        binding.addBt.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_farmingFragment_to_fillDataFragment)
        }



        return binding.root
    }




    fun notInUser(){
//        val mainViewModel : MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//
//        val userData = mainViewModel.getUser()
//        userData.observe(viewLifecycleOwner) {
//            binding.kisanName.text = it.name
//            binding.kisanLocation.text = it.location
//            binding.loanTT.text = it.loan?.toString() ?: "N/A"
//            binding.farmSize.text = it.farm?.farmSize.toString()
//            binding.farmMoisture.text = it.farm?.moisture.toString()
//            binding.farmSoil.text = it.farm?.soilType.toString()
//
//        }
//        binding.weatherBt.setOnClickListener {
//            if (isAdded){
//                startActivity(Intent(context,weatherActivity::class.java))
//            }
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
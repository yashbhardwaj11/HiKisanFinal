package com.devinfusion.hikisanfinal.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.activities.MessageActivity
import com.devinfusion.hikisanfinal.databinding.FragmentMarketBinding


class MarketFragment : Fragment() {
    private var _binding : FragmentMarketBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarketBinding.inflate(layoutInflater,container,false)
        startActivity(Intent(requireActivity(),MessageActivity::class.java))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
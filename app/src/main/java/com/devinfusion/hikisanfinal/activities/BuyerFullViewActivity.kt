package com.devinfusion.hikisanfinal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.databinding.ActivityBuyerFullViewBinding
import com.devinfusion.hikisanfinal.databinding.FragmentBuyerFullViewBinding

class BuyerFullViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyerFullViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerFullViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val companyName = intent?.getStringExtra("companyName")
        val companyPhoto = intent?.getStringExtra("companyPhoto")
        val desc = intent?.getStringExtra("desc")
        val title = intent?.getStringExtra("title")
        val price = intent?.getStringExtra("price")

        binding.companyImage.load(companyPhoto)
        binding.name.text = companyName
        binding.farmSize.text = title
        binding.soil.text = desc
        binding.moisture.text = price

    }
}
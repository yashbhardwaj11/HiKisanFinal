package com.devinfusion.hikisanfinal.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.viewModel.ViewFarmViewModel
import com.devinfusion.hikisanfinal.dao.UserDao
import com.devinfusion.hikisanfinal.databinding.FragmentViewFarmBinding
import com.devinfusion.hikisanfinal.model.Farm
import com.devinfusion.hikisanfinal.model.FarmSell
import com.devinfusion.hikisanfinal.model.Kisan
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewFarmFragment : Fragment() {

    private var _binding : FragmentViewFarmBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewFarmBinding.inflate(layoutInflater,container,false)

        var getBundle: Bundle? = null
        getBundle = requireActivity().getIntent().getExtras()
        val farmId = getBundle?.getString("farmId")



        binding.buyBt.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val userDao = UserDao()
                val user = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                    .addOnCompleteListener{
                        if (it.isSuccessful) {
                            val snapshot = it.result
                            // Use the snapshot to extract user data
//                    val farm = snapshot.child("farm").value as Farm
                            val location = snapshot.child("location").value as String
                            val mobileNumber = snapshot.child("mobileNumber").value as String
                            val age = snapshot.child("age").value as String
                            val name = snapshot.child("name").value as String
                            val loan = snapshot.child("loan").value as Long

                            val user = Kisan(FirebaseAuth.getInstance().currentUser!!.uid,
                            FirebaseAuth.getInstance().currentUser!!.phoneNumber,
                                name,
                                age,
                                location,
                                loan.toInt(),
                                farmId,
                                Farm(name,
                                "20","92%",
                                "Saline")
                                )

                            userDao.userCollection.child(FirebaseAuth.getInstance().currentUser!!.uid.toString()).setValue(user)
                                .addOnCompleteListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "You have successfully booked the farm soon you will receive all the details",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Navigation.findNavController(requireView()).navigate(R.id.action_viewFarmFragment_to_farmingFragment)

                                }
                                .addOnFailureListener {
                                    Toast.makeText(requireContext(), "Failed ${it.message}", Toast.LENGTH_SHORT).show()
                                }

                        } else {
                            // Handle errors
                        }
                    }
            }
        }


        val viewFarmViewModel : ViewFarmViewModel = ViewModelProvider(this).get(ViewFarmViewModel::class.java)
        val farmList : LiveData<FarmSell> = viewFarmViewModel.getUser()
        farmList.observe(viewLifecycleOwner){
            binding.soil.text = it.farmSoil
            binding.farmSize.text = it.farmSize
            binding.moisture.text = it.farmMoisture
            binding.name.text = it.farmerName
            binding.price.text = "₹${it.farmPrice}"
            val price : Int =  it.farmPrice!!.toInt()
            val size : Int = it.farmSize!!.toInt()
            val total  =price*size
            binding.totalPrice.text = "₹${total.toString()}"
        }
        return binding.root
    }

}
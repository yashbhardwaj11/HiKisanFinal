package com.devinfusion.hikisanfinal.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.dao.UserDao
import com.devinfusion.hikisanfinal.databinding.FragmentFillDataBinding
import com.devinfusion.hikisanfinal.model.FarmSell
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FillDataFragment : Fragment() {
    private var _binding : FragmentFillDataBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFillDataBinding.inflate(layoutInflater,container,false)

        binding.sendBt.setOnClickListener {
            Log.d("Reached","reahedinsendbt")
            uploadDataToFirebase(binding.farmSizeEditText.text.toString().trim(),binding.farmPriceEditText.text.toString().trim())
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun uploadDataToFirebase(farmSize: String, farmPrice: String) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid

        val userDao = UserDao()
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("Reached","reahedinsendbtllll")

            userDao.getUserById(userId).addOnCompleteListener {
                if (it.isSuccessful) {
                    val snapshot = it.result
                    // Use the snapshot to extract user data
//                    val farm = snapshot.child("farm").value as Farm
                    val location = snapshot.child("location").value as String
                    val name = snapshot.child("name").value as String

                    val pushKey = userDao.farmCollection.push().key.toString()

                    val farmSell = FarmSell(userId,name,farmSize,farmPrice,location,"Saline","92%",pushKey)

                    FirebaseDatabase.getInstance().getReference("Farm").child(userId).setValue(farmSell).addOnCompleteListener {
                        Toast.makeText(requireContext(), "Data uploaded success", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(requireView()).navigate(R.id.action_fillDataFragment_to_farmingFragment)
                    }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                            Log.d("Reached",it.message.toString())

                        }

                    // ...
                } else {
                    // Handle errors
                }
            }
        }

    }


}
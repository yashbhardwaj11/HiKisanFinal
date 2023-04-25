package com.devinfusion.hikisanfinal.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.devinfusion.hikisanfinal.R
import com.devinfusion.hikisanfinal.activities.MessageActivity
import com.devinfusion.hikisanfinal.databinding.FragmentHomeBinding
import com.devinfusion.hikisanfinal.model.WeatherRvModel
import com.google.android.gms.location.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var PERMISSION_ID : Int = 42
    private lateinit var showMoreBT : Button
    private lateinit var api_key : String
    private lateinit var context : Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)

        api_key = "d14ce72bc6f2849598263e88bf643943"

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (isInternetAvailable()){
            getLastLocation()
            Log.d("responses","Method calling in internet")
        }
        else{
            val message = "No internet connection"
            if(message.isNotEmpty()){
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                Log.d("responses","Method calling")

            }
        }
        binding.farmBt.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_farmingFragment)
        }
        binding.shopBt.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_shopFragment)
        }
        binding.messageBt.setOnClickListener {
            startActivity(Intent(requireContext(),MessageActivity::class.java))
        }

        getLastLocation()

        return binding.root
    }

    @SuppressLint("MissingPermission")
    fun isInternetAvailable(): Boolean {
        val cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    override fun onStart() {
        super.onStart()
        getLastLocation()
    }



    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private fun checkPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        Log.d("responses","getLast")
        if (checkPermission()) {
            Log.d("responses","getLast check")

            mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                var location: Location? = task.result
                if (location == null) {
                    Toast.makeText(requireContext(), "Location is null", Toast.LENGTH_SHORT).show()
                    requestNewLocationData()
                    Log.d("responses","getLast location recieved not")

                } else {
//                        Toast.makeText(applicationContext, "${location.latitude.toString()}", Toast.LENGTH_SHORT).show()
                    startFillingLocation(location.latitude,location.longitude)
                    Log.d("responses","getLast ${location.latitude}")

                }
            }

//            if (isLocationEnable()) {
//                Log.d("responses","getLast location")
//
//
//
//            } else {
//                Toast.makeText(requireContext(), "Turn the location on", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null)
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation!!
            startFillingLocation(mLastLocation.latitude,mLastLocation.longitude)
        }
    }

    private fun startFillingLocation(latitude: Double, longitude: Double) {
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$api_key"

        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                binding.progressBarHelper.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
//                Toast.makeText(applicationContext, "${response.toString()}", Toast.LENGTH_SHORT).show()
                pushingValues(response)

            }, {
                Toast.makeText(requireContext(), it.printStackTrace().toString(), Toast.LENGTH_SHORT).show()

            })

        requestQueue.add(jsonObjectRequest)
    }



    private fun pushingValues(response: JSONObject?) {
        val Cityname = response?.getString("name")
        val main = response?.getJSONObject("main")
        val temp = main?.getInt("temp")
        val temp_min = main?.getInt("temp_min")
        val temp_max = main?.getInt("temp_max")
        val temp_minV = temp_min as Int
        val temp_min_final = temp_minV-273
        val temp_maxV = temp_max as Int
        val temp_max_final = temp_maxV-273 + 1
        val sys = response.getJSONObject("sys")
        val sunrise = sys.getLong("sunrise")
        val sunset = sys.getLong("sunset")
        val humidity = main.getString("humidity")

        val wind = response.getJSONObject("wind")
        val windSpeed = wind.getString("speed")

        val weather = response.getJSONArray("weather").getJSONObject(0)
        val forecast = weather.getString("main")
        val forecastIcon = "http://openweathermap.org/img/wn/${weather.getString("icon")}.png"
        Log.d("forecastIcon", forecastIcon)

        val mainTemp : Int = temp!! - 273

        val date = response.getLong("dt")



        val weatherModel = WeatherRvModel(0,Cityname!!,windSpeed, humidity,mainTemp,temp_max_final.toString(),temp_min_final.toString(),
            forecastIcon,forecast, date,"current")

        binddata(weatherModel)

    }

    private fun binddata(weatherModel: WeatherRvModel) {
        val final = longToDate(weatherModel.lastupdatedAt)

        binding.tempMax.text = "${weatherModel.temp_max}°C"
        binding.tempMin.text = "${weatherModel.temp_min}°C"
        binding.location.text = weatherModel.name
        binding.windTT.text = weatherModel.speed


//        binding.minTemp.text = "min temp : ${weatherModel.temp_min.toString()}°C"
//        binding.maxTemp.text = "max temp : ${weatherModel.temp_min.toString()}°C"
    }

    private fun longToDate(time: Long): String {
        val format = "dd MMM yyyy HH:mm"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(time * 1000))
    }





    fun notInUser(){
//        val mainViewModel : MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

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
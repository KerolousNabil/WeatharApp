package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewtest.MyAdapter
import com.example.weather_app.weeather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var textallow:TextView
    private lateinit var resultx : TextView
    private lateinit var namecity: TextView
    private lateinit var iconweather : ImageView
    private lateinit var iconlocation : ImageView
    private lateinit var linearwave:LinearLayout
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 42

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermissions()



        textallow = findViewById(R.id.textpermission)
        textallow.visibility = View.GONE
        iconlocation = findViewById(R.id.iconlocation)
        linearwave = findViewById(R.id.linearwave)
        resultx = findViewById(R.id.tvSunSetTime)
        namecity = findViewById(R.id.name_of_city)
        iconweather = findViewById(R.id.imageView_icon_weather)
        userRecyclerview = findViewById(R.id.recyclerView)
        userRecyclerview.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        userRecyclerview.setHasFixedSize(true)

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search for any city ......"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {


                val searchtext = query!!.lowercase(Locale.getDefault())

                if (searchtext.isNotEmpty())
                {

                    loadWeatherByCityName(searchtext)



                }


                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun loadimage(icon:String) {
        Ion.with(this)
            .load("https://www.weatherbit.io/static/img/icons/$icon.png")
            .intoImageView(iconweather)

    }



    @SuppressLint("ResourceType")
    private fun loadWeatherByCityName(city: String) {
        Ion.with(this)
            .load("https://api.weatherbit.io/v2.0/current?lat=35.7796&lon=-78.6382&key=7833f0c62225411a9f17c9ba84459d26&include=minutely&city=$city")
            .asJsonObject()
            .setCallback(FutureCallback<JsonObject?> { e, result ->
                if(e!= null)
                {
                    e.printStackTrace()
                    Toast.makeText(this , "Server error" , Toast.LENGTH_SHORT).show()
                }
                else
                {
                    var data : JsonArray = result.get("data").asJsonArray


                    data.forEach { item ->
                        //     Log.d("gggggggggggg", item.asJsonObject.get("main").toString())
                        resultx.text = item.asJsonObject.get("temp").toString() +"°C"

                    }

                    //      var main : JsonObject? = result.get("weather").asJsonArray.get(0).asJsonObject


                    //Log.d("rrrrrrrrrrrrr", description?.get("main").toString())

                    // resultx.text = main?.get("main").toString()


                    //  var stret = coord.get("speed").asString
                    //
                    //      resultx.text = stret.toString()
                    var data_name : JsonArray = result.get("data").asJsonArray
                    var city_name: String? = data_name.get(0).asJsonObject.get("city_name").asString
                    namecity.text = city_name.toString()

                    var d : JsonArray = result.get("data").asJsonArray
                    var weather: JsonObject = d.get(0).asJsonObject.get("weather").asJsonObject
                    var icon :String = weather.get("icon").asString
                    loadimage(icon)








                    //  linearLayout.setBackgroundResource(R.drawable.light)


                    //     linearLayout.setBackgroundResource(R.drawable.sunny)
                    //var time = 1


                    var data1 : JsonArray = result.get("data").asJsonArray
                    var lon: Double = data1.get(0).asJsonObject.get("lon").asDouble

                    var data2 : JsonArray = result.get("data").asJsonArray
                    var lat: Double = data2.get(0).asJsonObject.get("lat").asDouble
                    loaddailyforcast(lon,lat)

                }
            })
    }
    private fun loaddailyforcast(lon: Double, lat:Double) {

        Ion.with(this)
            .load("https://api.weatherbit.io/v2.0/forecast/daily?&key=7833f0c62225411a9f17c9ba84459d26&lat=$lat&lon=$lon")
            .asJsonObject()
            .setCallback(FutureCallback<JsonObject?> { e, result ->
                if(e!= null)
                {
                    e.printStackTrace()
                    Toast.makeText(this , "Server error" , Toast.LENGTH_SHORT).show()
                }
                else
                {

                    val weatherList: ArrayList<weeather> = ArrayList()

                    var timezone: String? =  result.get("timezone").asString;
                    var all_data : JsonArray = result.get("data").asJsonArray
                    for (i in 1 until all_data.size())
                    {

                        var date: Long? = all_data.get(i).asJsonObject.get("moonrise_ts").asLong
                        var temp: String? = all_data.get(i).asJsonObject.get("temp").asString
                        var icon :String = all_data.get(i).asJsonObject.get("weather").asJsonObject.get("icon").asString
                        weatherList.add(
                            weeather(date, timezone.toString(),
                            temp.toString(), icon)
                        )

                    }

                    val weatherAdapter = MyAdapter(this@MainActivity, weatherList)
                    userRecyclerview.adapter = weatherAdapter


                }
            })


    }


    private fun checkPermissions() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        try {
                            Ion.with(this)
                                .load("https://api.weatherbit.io/v2.0/forecast/daily?&key=7833f0c62225411a9f17c9ba84459d26&lat=${location.latitude}&lon=${location.longitude}")
                                .asJsonObject()
                                .setCallback(FutureCallback<JsonObject?> { e, result ->

                                    if(e!= null)
                                    {
                                        e.printStackTrace()
                                        Toast.makeText(this , "Server error" , Toast.LENGTH_SHORT).show()
                                    }
                                    else
                                    {
                                        val weatherList: ArrayList<weeather> = ArrayList()

                                        var timezone: String? =  result.get("timezone").asString;
                                        var all_data : JsonArray = result.get("data").asJsonArray
                                        for (i in 1 until all_data.size())
                                        {

                                            var date: Long? = all_data.get(i).asJsonObject.get("moonrise_ts").asLong
                                            var temp: String? = all_data.get(i).asJsonObject.get("temp").asString
                                            var icon :String = all_data.get(i).asJsonObject.get("weather").asJsonObject.get("icon").asString

                                            weatherList.add(weeather(date, timezone.toString(),
                                                temp.toString(), icon))



                                        }



                                        val weatherAdapter = MyAdapter(this@MainActivity, weatherList)
                                        userRecyclerview.adapter = weatherAdapter


                                    }


                                })

                            Ion.with(this)
                                .load("https://api.weatherbit.io/v2.0/current?lat=${location.latitude}6&lon=${location.longitude}&key=7833f0c62225411a9f17c9ba84459d26&include=minutely&")
                                .asJsonObject()
                                .setCallback(FutureCallback<JsonObject?> { e, result ->
                                    if(e!= null)
                                    {
                                        e.printStackTrace()
                                        Toast.makeText(this , "Server error" , Toast.LENGTH_SHORT).show()
                                    }
                                    else
                                    {
                                        var data : JsonArray = result.get("data").asJsonArray

                                        data.forEach { item ->
                                            resultx.text = item.asJsonObject.get("temp").toString() +"°C"

                                        }

                                        var data_name : JsonArray = result.get("data").asJsonArray
                                        var city_name: String? = data_name.get(0).asJsonObject.get("city_name").asString
                                        namecity.text = city_name.toString()

                                        var d : JsonArray = result.get("data").asJsonArray
                                        var weather: JsonObject = d.get(0).asJsonObject.get("weather").asJsonObject
                                        var icon :String = weather.get("icon").asString
                                        loadimage(icon)

                                        var data1 : JsonArray = result.get("data").asJsonArray
                                        var lon: Double = data1.get(0).asJsonObject.get("lon").asDouble

                                        var data2 : JsonArray = result.get("data").asJsonArray
                                        var lat: Double = data2.get(0).asJsonObject.get("lat").asDouble
                                        loaddailyforcast(lon,lat)





                                    }
                                })



                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
        } else {
            askPermission()
        }



    }
    private fun askPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                checkPermissions()

            }
            else {

                textallow.visibility = View.VISIBLE
                iconlocation.visibility = View.GONE
                linearwave.visibility = View.GONE

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }





}
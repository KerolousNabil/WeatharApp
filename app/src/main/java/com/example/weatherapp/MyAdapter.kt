package com.example.recyclerviewtest

import android.content.Context
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.weeather
import com.example.weatherapp.R
import com.koushikdutta.ion.Ion
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(var mycontext : Context, private val userList : ArrayList<weeather>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_weather,
            parent,false)
        return MyViewHolder(itemView)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val (date1, timezone, temp, icon) = userList[position]


       // holder.tvDate.text = date1.toString()
     //   holder.tvTemp.text = temp
        holder.tvTemp.text = "$temp °C"
        Ion.with(mycontext)
            .load("https://www.weatherbit.io/static/img/icons/$icon.png")
            .intoImageView(holder.Iconweather)
        val date = Date(date1!! * 1000)
        val dateFormat: DateFormat = SimpleDateFormat("EEE, MMM ,dd", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone(timezone)
        holder.tvDate.text = dateFormat.format(date)

    }

    override fun getItemCount(): Int {

        return userList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

          val tvDate: TextView = itemView.findViewById(R.id.tvDate)
          val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
          val Iconweather: ImageView = itemView.findViewById(R.id.iconWeather)



    }

}
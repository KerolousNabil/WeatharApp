package com.example.weather_app

import android.content.Context
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.weatherapp.R
import com.koushikdutta.ion.Ion
import java.util.*

 class WeatherAdapter(
    private val mycontext: Context,
    private val weatherList: List<weeather>
) :
    ArrayAdapter<weeather?>(mycontext, 0, weatherList) {
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView = LayoutInflater.from(mycontext).inflate(R.layout.item_weather, parent, false)
        val tvDate = convertView.findViewById<TextView>(R.id.tvDate)
        val tvTemp = convertView.findViewById<TextView>(R.id.tvTemp)
        val Iconweather = convertView.findViewById<ImageView>(R.id.iconWeather)
        val (date1, timezone, temp, icon) = weatherList[position]
        tvTemp.text = "$temp °C"
        Ion.with(mycontext)
            .load("https://www.weatherbit.io/static/img/icons/$icon.png")
            .intoImageView(Iconweather)
        val date = Date(date1!! * 1000)
        val dateFormat: DateFormat = SimpleDateFormat("EEE, MMM ,dd", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone(timezone)
        tvDate.text = dateFormat.format(date)
        return convertView
    }
}

package com.example.weatherapp;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.weather_app.weeather;import com.koushikdutta.ion.Ion;

import java.util.Date;
import java.util.List;
import java.util.Locale;


class DailyWeatherAdapter extends ArrayAdapter<weeather> {

    private Context context;
    private List<weeather>  weatherList;

    public DailyWeatherAdapter(@NonNull Context context, @NonNull List<weeather> weatherList) {
        super(context, 0, weatherList);
        this.context = context;
        this.weatherList = weatherList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_weather, parent, false);

        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvTemp = convertView.findViewById(R.id.tvTemp);
        ImageView Iconweather = convertView.findViewById(R.id.iconWeather);

        weeather weather = weatherList.get(position);
        tvTemp.setText(weather.getTemp()+" °C");

        Ion.with(context)
                .load("https://www.weatherbit.io/static/img/icons/"+weather.getIcon()+".png")
                .intoImageView(Iconweather);

        Date date = new Date(weather.getDate() *1000 );
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM ,dd", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(weather.getTimezone()));
        tvDate.setText(dateFormat.format(date));

        return convertView;
    }

}

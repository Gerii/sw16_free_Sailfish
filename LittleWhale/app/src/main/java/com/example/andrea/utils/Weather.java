package com.example.andrea.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.andrea.littewhale.NavigationActivity;
import com.example.andrea.littewhale.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.json.*;

public class Weather {
    private static final String API_KEY = "d1db7d9ac0033228ce578944c83ac06a";
    private static HashMap<Integer, String> WEATHER_CODES;


    private int id = -1;
    private String weatherIcon;

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }


    public Calendar getDate() {
        return date;
    }

    public int getClouds() {
        return clouds;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    private String shortDescription;
    private String detailedDescription;
    private Calendar date;
    private int clouds;
    private int humidity;
    private double pressure;
    private double temperature;
    private double windSpeed;
    private double windDirection;

    private static void loadWeatherIds(Context context) {
        if (WEATHER_CODES == null) {
            String[] weatherIconCodes = context.getResources().getStringArray(R.array.weathericoncode);
            int[] weatherIds = context.getResources().getIntArray(R.array.weatherid);
            assert (weatherIconCodes.length == weatherIds.length);

            WEATHER_CODES = new HashMap<>();
            for (int i = 0; i < weatherIconCodes.length; ++i) {
                WEATHER_CODES.put(weatherIds[i], weatherIconCodes[i]);
            }
        }
    }

    public Weather(Context context) {
        loadWeatherIds(context);
    }

    public void parseWeather(String response) throws WeatherParsingException {
        Log.e("res", response   );
        try {
            JSONObject responseJSON = new JSONObject(response);
            JSONArray weatherJSONArray = (JSONArray) responseJSON.get("weather");
            JSONObject firstWeatherJSON = (JSONObject) weatherJSONArray.get(0);

            this.id = Integer.parseInt(firstWeatherJSON.get("id").toString());
            this.shortDescription = firstWeatherJSON.get("main").toString();
            this.detailedDescription = firstWeatherJSON.get("description").toString();

            this.weatherIcon = WEATHER_CODES.get((id == 800) ? id : id / 100);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(responseJSON.get("dt").toString() + "000"));

            this.date = cal;

            JSONObject clouds = (JSONObject) responseJSON.get("clouds");
            this.clouds = Integer.parseInt(clouds.get("all").toString());

            JSONObject main = (JSONObject) responseJSON.get("main");
            this.humidity = Integer.parseInt(main.get("humidity").toString());
            this.pressure = Double.parseDouble(main.get("pressure").toString());
            this.temperature = Double.parseDouble(main.get("temp").toString()) - 273.15;

            JSONObject wind = (JSONObject) responseJSON.get("wind");
            this.windSpeed = Double.parseDouble(wind.get("speed").toString());
            this.windDirection = Double.parseDouble(wind.get("deg").toString());
        } catch (JSONException e) {
            Log.e("Weather Parsing", e.toString());
            throw new WeatherParsingException();
        }
    }

    public void updateWeather(double lat, double lon, NavigationActivity.SectionsPagerAdapter mSectionsPagerAdapter) {
        final String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + Double.toString(lat) + "&lon=" + Double.toString(lon) + "&appid=" + API_KEY;
        WeatherGetterWhatever wgw = new WeatherGetterWhatever(this, url, mSectionsPagerAdapter);
        wgw.execute(lat, lon);
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

}

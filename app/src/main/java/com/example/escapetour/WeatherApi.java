package com.example.escapetour;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class WeatherApi {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "2f7886bdb8df0225fdd947146478988c";

    public static void getCurrentWeather(double latitude, double longitude, final OnWeatherDataListener listener) {
        String url = String.format(Locale.US, "%s?lat=%f&lon=%f&units=metric&appid=%s&exclude=minutely,hourly,daily,alerts", BASE_URL, latitude, longitude, API_KEY);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main = response.getJSONObject("main");
                            double temperature = main.getDouble("temp");
//                            double temp_max = main.getDouble("temp_max");
//                            double temp_min = main.getDouble("temp_min");
                            int humidity = main.getInt("humidity");
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObject = weatherArray.getJSONObject(0);
                            String icon = weatherObject.getString("icon");
//                            String condition = "";
//                            int conditionId = main.getInt("id");
//                            if (conditionId == 800) {
//                                condition = "Clear";
//                            } else if (conditionId >= 200 && conditionId <= 232) {
//                                condition = "Thunderstorm";
//                            } else if (conditionId >= 300 && conditionId <= 321) {
//                                condition = "Drizzle";
//                            } else if (conditionId >= 500 && conditionId <= 531) {
//                                condition = "Rain";
//                            } else if (conditionId >= 600 && conditionId <= 622) {
//                                condition = "Snow";
//                            } else if (conditionId >= 701 && conditionId <= 781) {
//                                condition = "Atmosphere";
//                            } else if (conditionId >= 801 && conditionId <= 804) {
//                                condition = "Clouds";
//                            }


                            if (listener != null) {
                                listener.onWeatherDataReceived(temperature, humidity, icon);
//                                listener.onWeatherDataReceived(temp_max);
//                                listener.onWeatherDataReceived(temp_min);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (listener != null) {
                            listener.onWeatherDataError(error.getMessage());
                        }
                    }
                });

        Volley.newRequestQueue(listener.getContext()).add(request);
    }

    public interface OnWeatherDataListener {
        void onWeatherDataReceived(double temperature, int humidity, String icon);

        void onWeatherDataError(String error);

        Context getContext();
    }
}

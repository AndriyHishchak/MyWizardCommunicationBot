package com.home.MyWizardBot.Service;

import com.home.MyWizardBot.botApi.BotState;
import com.home.MyWizardBot.cache.BotDataCache;
import com.home.MyWizardBot.model.Weather;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
@Service
public class ServiceWeather {

    public Weather getWeather(String message, Weather weather) throws IOException {

        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+message+"&units=metric&appid=b805ebd38f800bf523655a502bb5be62");


        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
        result += (in.nextLine());
        }


        JSONObject object = new JSONObject(result);
        weather.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        weather.setTemp(main.getDouble("temp"));
        weather.setHumidity(main.getDouble("humidity"));

        JSONArray getArrey = object.getJSONArray("weather");
        for (int i = 0; i < getArrey.length();i++){
            JSONObject obj = getArrey.getJSONObject(i);
            weather.setIcon((String) obj.get("icon"));
            weather.setMain((String) obj.get("main"));
        }

        return weather;
    }
}

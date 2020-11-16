package com.home.WeatherBot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Weather {

    String name;
    Double temp;
    Double humidity;
    String icon;
    String main;

    @Override
    public String toString() {
        return  " __________________________________________________________________" + "\n" +
                "| City : " + name + " \uD83C\uDFD9 \n" +
                "| Main : " + main + " ☁️\n" +
                "| Temperature : " + temp + "C \uD83D\uDCCD" + " \n" +
                "| Humidity : " + humidity + "% \uD83D\uDCA6" + "\n" +
                "| "+"http://openweathermap.org/img/wn/" + icon + ".png" + "\n" +
                "| Єсли снова хотите узнать погоду [ /show_weather ]" + "\n" +
                " --------------------------------------------------------------------------------------------"
                ;
    }
}

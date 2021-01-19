package com.home.WeatherBot.Service;

import com.home.WeatherBot.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User save (User user);

}

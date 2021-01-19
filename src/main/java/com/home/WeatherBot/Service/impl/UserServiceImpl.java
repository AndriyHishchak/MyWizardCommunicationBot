package com.home.WeatherBot.Service.impl;

import com.home.WeatherBot.Repo.UserRepo;
import com.home.WeatherBot.Service.UserService;
import com.home.WeatherBot.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepository;

    public UserServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }



}

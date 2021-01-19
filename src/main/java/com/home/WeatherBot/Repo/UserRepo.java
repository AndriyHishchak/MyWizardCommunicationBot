package com.home.WeatherBot.Repo;

import com.home.WeatherBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {


   Boolean existsByIdUser (long userId);
   User findByIdUser (long userId);



}

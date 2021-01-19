package com.home.WeatherBot.Repo;

import com.home.WeatherBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepo extends JpaRepository<User,Long> {

   @Query("SELECT u FROM User u WHERE u.subscription = true")
   List<User> getSubscription(Boolean subscription);

   Boolean existsByIdUser (long userId);
   User findByIdUser (long userId);



}

package com.home.WeatherBot.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.GregorianCalendar;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long idUser;
    private String city;
    private LocalTime timeOfNotification;
    private boolean subscription;
    private GregorianCalendar dateUpdate;

    public User(long idUser, String city, LocalTime timeOfNotification, boolean subscription,GregorianCalendar dateUpdate) {
        this.idUser = idUser;
        this.city = city;
        this.timeOfNotification = timeOfNotification;
        this.subscription = subscription;
        this.dateUpdate = dateUpdate;
    }
    public User(){}
}

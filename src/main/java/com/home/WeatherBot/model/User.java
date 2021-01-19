package com.home.WeatherBot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class User  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long idUser;
    private String city;
    private LocalTime timeOfNotification;
    private boolean subscription;
    private GregorianCalendar dateUpdate;


    public User(long idUser, String city, LocalTime timeOfNotification, boolean subscription, GregorianCalendar dateUpdate) {
        this.idUser = idUser;
        this.city = city;
        this.timeOfNotification = timeOfNotification;
        this.subscription = subscription;
        this.dateUpdate = dateUpdate;


    }

    public User(long idUser, String city, boolean subscription, GregorianCalendar dateUpdate) {
        this.idUser = idUser;
        this.city = city;
        this.subscription = subscription;
        this.dateUpdate = dateUpdate;
    }

    public User(){}


    public User(long idUser, GregorianCalendar dateUpdate) {
        this.idUser = idUser;
        this.dateUpdate = dateUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getIdUser() == user.getIdUser();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdUser());
    }
}


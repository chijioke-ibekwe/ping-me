package com.project.pingme.util;

import com.project.pingme.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {

    public static String formatDateTime(LocalDateTime localDateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
            return localDateTime.format(formatter);
        } catch (Exception e) {
            return "N/A";
        }
    }

    public static String formatUserFullName(User user){
        return user.getFirstName() + " " + user.getLastName();
    }
}

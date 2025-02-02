package com.example.discordbackend.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserUtils {

    public static String getCurrentUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

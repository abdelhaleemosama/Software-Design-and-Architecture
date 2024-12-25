package com.travelagency.notification.models;

import org.springframework.stereotype.Component;

public abstract class Method {
    public abstract void send(String message);
}


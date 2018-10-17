package edu.ua.cs.acm.messages;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class UnsubscribeMessage {
    private String email;

    @JsonSerialize(using = ToStringSerializer.class)

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

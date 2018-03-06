package edu.ua.cs.acm.messages;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Created by Hunter Allen on 3/5/18
 *
 * Easier to serialize the JSON to a String with a message class
 */
public class IsPaidMessage {
    private String email;

    @JsonSerialize(using = ToStringSerializer.class)

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}

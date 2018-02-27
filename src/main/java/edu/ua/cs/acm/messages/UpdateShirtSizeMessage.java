package edu.ua.cs.acm.messages;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Created by Jared Cleghorn on 2/19/18.
 */
public class UpdateShirtSizeMessage {
    private String email;
    private String newShirtSize;

    // @JsonSerialize(using = ToStringSerializer.class)

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewShirtSize() {
        return newShirtSize;
    }

    public void setNewShirtSize(String shirtSize) {
        this.newShirtSize = shirtSize;
    }
}

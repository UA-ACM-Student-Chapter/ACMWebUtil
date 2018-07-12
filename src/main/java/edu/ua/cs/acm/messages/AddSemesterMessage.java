package edu.ua.cs.acm.messages;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Created by Hunter Allen on 3/5/18
 *
 * Easier to serialize the JSON to a String with a message class
 */
public class AddSemesterMessage {
    private String secretKey;
    private String startDate;
    private String endDate;
    private String dueDate;

    @JsonSerialize(using = ToStringSerializer.class)

    public String getSecretKey() { return secretKey; }

    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getDueDate() { return dueDate; }

    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
}

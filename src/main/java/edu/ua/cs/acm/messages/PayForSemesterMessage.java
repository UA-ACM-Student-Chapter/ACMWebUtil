package edu.ua.cs.acm.messages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

/**
 * Created by Hunter Allen on 2/26/18
 **/
public class PayForSemesterMessage {
    private String email;
    private String purchaseID;
    private String size;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime datePaid;

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    public String getPurchaseID() { return this.purchaseID; }

    public void setPurchaseID(String purchaseID) { this.purchaseID = purchaseID; }

    public String getSize() { return  this.size; }

    public void setSize(String size) { this.size = size; }

    public LocalDateTime getDatePaid() { return this.datePaid; }

    public void setDatePaid(LocalDateTime datePaid) { this.datePaid = datePaid; }
}

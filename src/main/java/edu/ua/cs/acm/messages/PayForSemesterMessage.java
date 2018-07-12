package edu.ua.cs.acm.messages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

/**
 * Created by Hunter Allen on 2/26/18
 **/
public class    PayForSemesterMessage {
    private String email;
    private String purchaseID;
    private String size;
    private String datePaid;
    private String paymentType;
    private String last4;
    private String cardType;

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    public String getPurchaseID() { return this.purchaseID; }

    public void setPurchaseID(String purchaseID) { this.purchaseID = purchaseID; }

    public String getSize() { return  this.size; }

    public void setSize(String size) { this.size = size; }

    public String getDatePaid() { return this.datePaid; }

    public void setDatePaid(String datePaid) { this.datePaid = datePaid; }

    public String getPaymentType() { return  this.paymentType; }

    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    public String getLast4() { return  this.last4; }

    public void setLast4(String last4) { this.last4 = last4; }
    
    public String getCardType() { return  this.cardType; }

    public void setCardType(String cardType) { this.cardType = cardType; }
}

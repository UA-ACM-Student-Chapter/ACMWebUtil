package edu.ua.cs.acm.email;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jzarobsky on 9/4/17.
 */
public class PaymentConfirmationEmailMessage extends DirectEmailMessage {

    private final String firstName;
    private final String lastName;
    private final String datePaid;
    private final String amountPaid;
    private final String paymentType;
    private final String purchaseID;
    private final String hiddenCCNumber;
    private final String cardType;
    private static final String TEMPLATE_NAME = "payment_confirmation.html";

    public PaymentConfirmationEmailMessage(String firstName, String lastName, String email, String datePaid, String amountPaid, String paymentType, String purchaseID, String hiddenCCNumber, String cardType) {
        super(email, "[UA ACM] Thanks for Paying Dues!", 5);
        setHtml(true);
        this.firstName = firstName;
        this.lastName = lastName;
        this.datePaid = datePaid;
        this.amountPaid = amountPaid;
        this.paymentType = paymentType;
        this.purchaseID = purchaseID;
        this.hiddenCCNumber = hiddenCCNumber;
        this.cardType = cardType;
    }

    @Override
    public String getBody() {
        Map<String, Object> model = new HashMap<>();
        model.put("firstName", getFirstName());
        model.put("lastName", getLastName());
        model.put("datePaid", getDatePaid());
        model.put("amountPaid", getAmountPaid());
        model.put("paymentType", getPaymentType());
        model.put("purchaseID", getPurchaseID());
        model.put("hiddenCCNumber", getHiddenCCNumber());
        model.put("cardType", getCardType());
        return renderTemplate(TEMPLATE_NAME, model);
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getDatePaid() {
        return datePaid;
    }

    public String getAmountPaid() { return amountPaid; }

    public String getPaymentType() { return paymentType; }

    public String getPurchaseID() { return purchaseID; }

    public String getHiddenCCNumber() { return hiddenCCNumber; }

    public String getCardType() { return cardType; }
}

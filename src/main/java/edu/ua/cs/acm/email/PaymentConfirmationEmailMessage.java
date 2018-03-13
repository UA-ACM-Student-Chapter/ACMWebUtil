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
    private static final String TEMPLATE_NAME = "payment_confirmation.html";

    public PaymentConfirmationEmailMessage(String firstName, String lastName, String email, String datePaid, String amountPaid) {
        super(email, "[UA ACM] Thanks for Paying Dues!", 5);
        setHtml(true);
        this.firstName = firstName;
        this.lastName = lastName;
        this.datePaid = datePaid;
        this.amountPaid = amountPaid;
    }

    @Override
    public String getBody() {
        Map<String, Object> model = new HashMap<>();
        model.put("firstName", getFirstName());
        model.put("lastName", getLastName());
        model.put("datePaid", getDatePaid());
        model.put("amountPaid", getAmountPaid());
        return renderTemplate(TEMPLATE_NAME, model);
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getDatePaid() {
        return datePaid;
    }

    public String getAmountPaid() { return amountPaid; }
}

package edu.ua.cs.acm.email;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * Created by jzarobsky on 9/4/17.
 */
public class DueReminderEmailMessage extends DirectEmailMessage {


    private final String firstName;
    private final LocalDateTime dueDate;
    private static final String TEMPLATE_NAME = "due_reminder.html";

    public DueReminderEmailMessage(String firstName, String email, LocalDateTime dueDate) {
        super(email, "[UA ACM] Pay Dues by " + dueDate.getMonthValue() + "-" + dueDate.getDayOfMonth(), 5);
        setHtml(true);
        this.firstName = firstName;
        this.dueDate = dueDate;
    }

    @Override
    public String getBody() {
        Map<String, Object> model = new HashMap<>();

        model.put("firstName", getFirstName());
        model.put("dueDate", getDueDate());
        return renderTemplate("due_reminder.html", model);
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }
}

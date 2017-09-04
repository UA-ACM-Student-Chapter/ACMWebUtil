package edu.ua.cs.acm.email;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jzarobsky on 9/4/17.
 */
public class JoinEmailMessage extends DirectEmailMessage {


    private final String firstName;
    private final String lastName;
    private final boolean includeSlackRegistration;
    private static final String TEMPLATE_NAME = "join.html";

    public JoinEmailMessage(String firstName, String lastName, String email, boolean includeSlackRegistration) {
        super(email, "[UA ACM] Welcome to ACM!", 5);
        setHtml(true);
        this.firstName = firstName;
        this.lastName = lastName;
        this.includeSlackRegistration = includeSlackRegistration;
    }

    @Override
    public String getBody() {
        Map<String, Object> model = new HashMap<>();

        model.put("firstName", getFirstName());
        model.put("lastName", getLastName());
        model.put("email", getToEmails()[0].split("@")[0]);
        model.put("includeSlack", includeSlackRegistration);

        return renderTemplate("join.html", model);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

package edu.ua.cs.acm.email;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jzarobsky on 9/4/17.
 */
public class ListservCommand extends DirectEmailMessage {


    private final String firstName;
    private final String lastName;
    private final String email;
    private final String command;
    private static final String TEMPLATE_NAME = "listserv_command.html";

    public ListservCommand(String firstName, String lastName, String email, String command) {
        super("listserv@bama.ua.edu", command , 5);
        setHtml(true);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.command = command;
    }

    @Override
    public String getBody() {
        Map<String, Object> model = new HashMap<>();

        model.put("firstName", getFirstName());
        model.put("lastName", getLastName());
        model.put("email", getEmail());
        model.put("command", getCommand());
        model.put("listservPassword", System.getenv("LISTSERV_PASSWORD"));
        return renderTemplate(TEMPLATE_NAME, model);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() { return email; }

    public String getCommand() { return command; }
}

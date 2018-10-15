package edu.ua.cs.acm.email;

import java.util.HashMap;
import java.util.Map;

public class ListservUnsubscribe extends DirectEmailMessage{
    private final String email;
    private static final String TEMPLATE_NAME = "listserv_unsubscribe.html";

    public ListservUnsubscribe(String email) {
        super("listserv@bama.ua.edu", null, 5);
        setHtml(true);
        this.email = email;
    }

    @Override
    public String getBody() {
        Map<String, Object> model = new HashMap<>();

        model.put("email", getEmail());
        model.put("command", "DEL");
        model.put("listservListname", System.getenv("LISTSERV_LIST_NAME"));
        model.put("listservePassword", System.getenv("LISTSERV_PASSWORD"));

        return renderTemplate(TEMPLATE_NAME, model);
    }

    public String getEmail() {
        return this.email;
    }
}

package edu.ua.cs.acm.services.impl;

import com.google.gson.*;
import edu.ua.cs.acm.services.CommonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * Created by damccoy on 07/13/18.
 */
@Service
public class CommonServiceImpl implements CommonService {

    private static final Gson objGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {

            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                Date date = Date.from(instant);
                return new JsonPrimitive(date.toString());
            }

    }).create();

    public ResponseEntity<Object> createResponse(String errorMessage, Map<String, Object> response) {
        response.put("errorMessage", errorMessage);
        return new ResponseEntity<>(objGson.toJson(response), HttpStatus.OK);
    }

    public boolean validateSecret(String secretKey) {
        return secretKey.equals(System.getenv("SECRET_KEY"));
    }

}

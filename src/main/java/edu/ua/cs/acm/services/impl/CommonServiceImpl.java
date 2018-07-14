package edu.ua.cs.acm.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ua.cs.acm.services.CommonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by damccoy on 07/13/18.
 */
@Service
public class CommonServiceImpl implements CommonService {

    private static final Gson objGson = new GsonBuilder().setPrettyPrinting().create();

    public ResponseEntity<Object> createResponse(String errorMessage, Map<String, Object> response) {
        response.put("errorMessage", errorMessage);
        return new ResponseEntity<>(objGson.toJson(response), HttpStatus.OK);
    }

}

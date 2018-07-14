package edu.ua.cs.acm.services;

import org.springframework.http.ResponseEntity;
import java.util.Map;

/**
 * Created by damccoy on 07/13/18.
 */
public interface CommonService {
    ResponseEntity<Object> createResponse(String errorMessage, Map<String, Object> response);
}

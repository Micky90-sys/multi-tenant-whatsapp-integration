package com.example.multitenantapp.service;

import com.example.multitenantapp.entity.Booking;
import com.example.multitenantapp.entity.User;
import com.example.multitenantapp.exception.ResourceNotFoundException;
import com.example.multitenantapp.repository.TenantRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsAppService {

    @Autowired
    private TenantRepository tenantRepository;

    private final String WHATSAPP_API_URL = "https://graph.facebook.com/v13.0/me/messages";

    public void sendBookingConfirmationMessage(User user, Booking booking) {
        String apiKey = user.getTenant().getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new ResourceNotFoundException("API key for tenant not found");
        }

        String phoneNumber = user.getPhoneNumber();

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new ResourceNotFoundException("Phone number for user not found");
        }

        String message = "Hi " + user.getName() + ", your booking for " + booking.getDetails() + " has been confirmed.";

        // Costruire il payload JSON per l'API di WhatsApp
        JSONObject payload = new JSONObject();
        payload.put("messaging_product", "whatsapp");
        payload.put("to", phoneNumber);
        payload.put("type", "template");
        
        JSONObject template = new JSONObject();
        template.put("name", "booking_confirmation");
        
        JSONObject language = new JSONObject();
        language.put("code", "en_US");
        
        template.put("language", language);
        
        JSONObject component = new JSONObject();
        component.put("type", "body");
        
        JSONObject parameter = new JSONObject();
        parameter.put("type", "text");
        parameter.put("text", message);
        
        template.put("components", new JSONArray().put(new JSONObject().put("type", "body").put("parameters", new JSONArray().put(parameter))));
        
        payload.put("template", template);

        // Configurare le intestazioni HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(payload.toString(), headers);

        // Inviare la richiesta HTTP
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(WHATSAPP_API_URL, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send WhatsApp message: " + response.getBody());
        }
    }
}

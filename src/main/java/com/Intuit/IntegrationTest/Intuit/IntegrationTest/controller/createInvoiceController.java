package com.Intuit.IntegrationTest.Intuit.IntegrationTest.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/quickbooks")
public class createInvoiceController {

    public static final String AUTHOROZATION_URL =  "https://appcenter.intuit.com/connect/oauth2";
    public static final String TOKEN_URL = "https://oauth.platform.intuit.com/oauth2/v1/tokens/bearer";
    public static final String BASE_URL = "https://sandbox-quickbooks.api.intuit.com";

    @Value("${quickbooks.clientId}")
    private String clientId;
    @Value("${quickbooks.clientSecret}")
    private String clientSecret;
    @Value("${quickbooks.redirectUri}")
    private String redirectUri;

    private String accessToken;
    private String refreshToken;

    @GetMapping("/custoauth")
    public ResponseEntity<?> authenticate() {
        String url = UriComponentsBuilder.fromUriString("https://appcenter.intuit.com/connect/oauth2")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "com.intuit.quickbooks.accounting")
                .queryParam("state", "testState")
                .toUriString();

        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, url).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String authenticationcode){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code&code=" + authenticationcode + "&redirect_uri=" + redirectUri;
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            accessToken = response.getBody().get("access_token").toString();
            refreshToken = response.getBody().get("refresh_token").toString();
            return ResponseEntity.ok("Access Token: " + accessToken);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to get access token");

    }

    @GetMapping("/generateInvoice")
    public ResponseEntity<> createInvoice(){
        String Url = BASE_URL + "/v3/company/9341453903497346/invoice?minorversion=75";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> = 
    }

}

package com.example.group_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestTemplateClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("http://localhost:8082/getAuth/")
    private String accountServiceUrl;

    @Value("${ADMIN_KEY}")
    private String personal_key;

    private MultiValueMap<String, String> headers;
    private HttpEntity<String> entity;

    RestTemplateClient() {
        headers = new LinkedMultiValueMap<>();
        personal_key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJVc2VyIGRldGFpbHMiLCJ1c2VybmFtZSI6IlRhbyIsImlkIjo0LCJST0xFIjoiUk9MRV9ISUdIIiwiaWF0IjoxNjk5NjEzODE4LCJpc3MiOiJtZXJvbmkiLCJleHAiOjIwNTk2MTM4MTh9.lEadKCrmESKfqx2-ghLxCeJGuLC20RvB4VJMy_rNMbU";
        headers.add("Authorization", "Bearer " + personal_key);

        entity = new HttpEntity<>("body", headers);
    }

    public List<Long> getListIdUsersOfGroup(Long idGroup) {
        try {
            ResponseEntity<List<Long>> response = restTemplate.exchange(
                    accountServiceUrl + "getListIdUsersGroup" + "?id=" + idGroup,
                    HttpMethod.GET, entity, new ParameterizedTypeReference<List<Long>>(){});
            return response.getBody();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
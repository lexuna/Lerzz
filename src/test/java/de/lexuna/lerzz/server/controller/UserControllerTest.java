package de.lexuna.lerzz.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.lexuna.lerzz.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPut;
import org.junit.jupiter.api.Test;
import org.springframework.http.RequestEntity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
class UserControllerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void loginIT() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = mapper.writeValueAsString(new User("user@mail", "name", "pw"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/signup"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        String json = mapper.writeValueAsString(new UserController.LoginRequest("user@mail", "pw"));

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/signin"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info(response.body().toString());
        assertEquals(200, response.statusCode());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/mail"))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void createIT() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = mapper.writeValueAsString(new User("mail", "name", "pw"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/signup"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info(response.body().toString());
        assertEquals(200, response.statusCode());


        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/mail"))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void update() {
    }
}
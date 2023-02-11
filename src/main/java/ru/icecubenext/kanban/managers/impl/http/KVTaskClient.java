package ru.icecubenext.kanban.managers.impl.http;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log4j
public class KVTaskClient {
    private final HttpClient httpClient;
    private final String API_TOKEN;
    private final String serverUri;

    public KVTaskClient(String serverUri) throws IOException, InterruptedException {
        this.serverUri = serverUri;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUri + "/register"))
                .GET()
                .build();
        httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        this.API_TOKEN = response.body();
    }

    public void put(String key, String json) {
        try {
            log.debug("POST: " + serverUri + "/save/" + key + "?API_TOKEN=" + API_TOKEN);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .uri(URI.create(serverUri + "/save/" + key + "?API_TOKEN=" + API_TOKEN))
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.debug("put key=" + key + " value=" + json + " Server answer:" +  response.statusCode());
        } catch (IOException | InterruptedException e) {
            log.debug("Во время выполнения операции put возникла ошибка");
        }
    }

    public String load(String key) {
        try {
            log.debug("GET: " + serverUri + "/load/" + key + "?API_TOKEN=" + API_TOKEN);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(serverUri + "/load/" + key + "?API_TOKEN=" + API_TOKEN))
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.debug("load key=" + key + " Server answer:" + response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            log.debug("Во время выполнения операции put возникла ошибка");
            return "";
        }
    }
}

package com.example.demo.service;

import com.example.demo.components.Components;
import com.example.demo.files.JsonFiles;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpHeaders;
import java.util.ArrayList;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class Server {
    public static ArrayList<Components> getComponentsList() throws JsonProcessingException {
        URL url = null;
        try {
            url = new URL("http://192.168.142.223:8080/api/main");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            StringBuilder stringBuilder = new StringBuilder();
            FileWriter fileWriter = new FileWriter("C:\\Projects\\hackatone_sber_2024\\elements.json", false);
            for (String line; (line = reader.readLine()) != null; ) {
                stringBuilder.append(line);
                fileWriter.write(line);
            }
            fileWriter.close();
            String str = stringBuilder.toString();
            System.out.println(str);
            ArrayList<Components> arrayList = JsonFiles.readPojoFromJson(str);

            System.out.println(arrayList);
            return arrayList;
        } catch (IOException e) {
            System.out.println("Parse exception");
            throw new RuntimeException(e);
        }
    }


    public static void sendElement(String json) {
        WebClient client = WebClient.create("http://192.168.142.223:8080/api");
        String requestJson = json;
        Mono<String> response = client.post()
                .uri("/addElementToAllUsers")
                .contentType(org.springframework.http.MediaType.valueOf(MediaType.APPLICATION_JSON))
                .body(BodyInserters.fromValue(requestJson))
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(response);
    }

    public static void deleteElement(String id) {
        WebClient client = WebClient.create("http://192.168.142.223:8080/api");
        String iden = id;
        Mono<String> response = client.post()
                .uri("/deleteElement")
                .contentType(org.springframework.http.MediaType.valueOf(MediaType.APPLICATION_JSON))
                .body(BodyInserters.fromValue(iden))
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(response);
    }
}

package com.MatOs.bot;

import com.MatOs.Main;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ProfileHandler {

    private static final String projectId = Main.dotenv.get("SUPABASE_ID");
    private static final String apiKey = Main.dotenv.get("SUPABASE_API_KEY");

    private static final String baseUrl = "https://" + projectId + ".supabase.co/rest/v1";
    private static final String tableName = "Profiles";

    private static final HttpClient client = HttpClient.newHttpClient();

    public static void insert(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String json = String.format("{\"name\": \"%s\", \"date_of_adding\": \"%s\"}", name, sdf.format(new Date()));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + tableName))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println("Response: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void delete(String name) {
        String encodedName = name.replace(" ", "%20");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + tableName + "?name=eq." + encodedName))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Prefer", "return=minimal")
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println("Delete response: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static HttpResponse<String> findUser(String userName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + tableName + "?select=*"))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if(response.body().contains(userName)) {
                return response;
            }else{
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static void updateCompletedTasks(String userName, int add){
        try {
            String bd = Objects.requireNonNull(findUser(userName)).body().trim();
            String json = "{\"tasks_completed\": \"" + (Integer.parseInt(bd.substring(bd.indexOf("tasks_completed")).replaceAll("[^0-9]", "")) + add) + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://" + projectId + ".supabase.co/rest/v1/Profiles?name=eq." + userName))
                    .header("apikey", apiKey)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("PATCH response: " + response.statusCode() + " — " + response.body());

        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
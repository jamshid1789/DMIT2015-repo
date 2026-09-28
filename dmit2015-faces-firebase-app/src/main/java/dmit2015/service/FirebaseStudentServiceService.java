package dmit2015.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * This class implements the FireBaseStudentServiceService using the HttpClient library to send Http Request to the Firebase Realtime Database REST API.
 */

@Named("firebaseHttpClientFireBaseStudentServiceService")
@ApplicationScoped
public class FirebaseStudentServiceService implements StudentService{
    /**
     * The base URL to the Firebase Realtime Database that is defined in `src/main/resources/META-INF/microprofile-config.properties` file.
     */
    @Inject
    @ConfigProperty(name = "firebase.rtdb.base.url")
    private String firebaseRtdbBaseUrl;

    /**
     * The URL to the Firebase Realtime Database to access all data.
     */
    private String jsonAllDataPath;

    /**
     * HttpClient is native Java library for sending Http Request to a web server
     */
    private HttpClient httpClient;

    /**
     * Jsonb is used for converting Java objects to a JSON string or visa-versa
     */
    private Jsonb jsonb;

    @PostConstruct
    private void init() {
        httpClient = HttpClient.newHttpClient();
        jsonb = JsonbBuilder.create();
    }

    /**
     * Pushing currentFireBaseStudentService data to Firebase Realtime Database using the REST API
     *
     * @link <a href="https://firebase.google.com/docs/reference/rest/database">Firebase Realtime Database REST API</a>
     */
    @Override
    public FireBaseStudentService createFireBaseStudentService(FireBaseStudentService fireBaseStudentService) {
        // Explicitly set the id value for the object to be created.
        fireBaseStudentService.setId(UUID.randomUUID().toString());

        // Build the url path to object to create
        jsonAllDataPath = String.format("%s/%s/%s.json",
                firebaseRtdbBaseUrl,
                FireBaseStudentService.class.getSimpleName(),
                fireBaseStudentService.getId());
        // Convert the Java object to a JSON string using JSONB
        String requestBodyJson = jsonb.toJson(fireBaseStudentService);

        // Create a Http Request for sending a Http POST request to push new data
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(jsonAllDataPath))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBodyJson, StandardCharsets.UTF_8))
                .build();
        try {
            // Send the Http Request
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // Check if the Http Request response is successful
            if (httpResponse.statusCode() == 200) {
                // Get the body of the Http Response
                var responseBodyJson = httpResponse.body();
                // Convert the JSON String to a FireBaseStudentService
                fireBaseStudentService = jsonb.fromJson(responseBodyJson, FireBaseStudentService.class);
            } else {
                String errorMessage = String.format("Create was not successful with status code: %s", httpResponse.statusCode());
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fireBaseStudentService;
    }

    @Override
    public Optional<FireBaseStudentService> getFireBaseStudentServiceById(String id) {
        // Build the url path to object to update
        String jsonSingleDataPath = String.format("%s/%s/%s.json",
                firebaseRtdbBaseUrl, FireBaseStudentService.class.getSimpleName(), id);
        try {
            // Create an GET Http Request to fetch all data
            var httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(jsonSingleDataPath))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .GET()
                    .build();
            // Send the GET Http Request
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // Check if the Http Request was successful
            if (httpResponse.statusCode() == 200) {
                // Get the body of the Http Response
                var responseBodyJson = httpResponse.body();
                if (!responseBodyJson.equals("null")) {
                    // Convert the responseBodyJson to an LinkedHashMap<String, FireBaseStudentService>
                    FireBaseStudentService responseData = jsonb.fromJson(responseBodyJson, FireBaseStudentService.class);
                    responseData.setId(id);
                    return Optional.of(responseData);
                } else {
                    throw new RuntimeException(String.format("FireBaseStudentService with id of %s not found", id));
                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<FireBaseStudentService> getAllFireBaseStudentServices() {
        // Build the url path to object to get all data
        jsonAllDataPath = String.format("%s/%s.json", firebaseRtdbBaseUrl, FireBaseStudentService.class.getSimpleName());
        // Create an GET Http Request to fetch all data
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(jsonAllDataPath))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            // Send the GET Http Request
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // Check if the Http Request was successful
            if (httpResponse.statusCode() == 200) {
                // Get the body of the Http Response
                var responseBodyJson = httpResponse.body();
                // Convert the responseBodyJson to an LinkedHashMap<String, FireBaseStudentService>
                LinkedHashMap<String, FireBaseStudentService> responseData = jsonb.fromJson(responseBodyJson, new LinkedHashMap<String, FireBaseStudentService>() {
                }.getClass().getGenericSuperclass());
                if (responseData != null) {
                    // Convert the LinkedHashMap<String, FireBaseStudentService> to List<FireBaseStudentService>
                    return responseData.entrySet()
                            .stream()
                            .map(item -> {
                                var currentFireBaseStudentService = item.getValue();
                                currentFireBaseStudentService.setId(item.getKey());
                                return currentFireBaseStudentService;
                            })
                            .toList();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }

    /**
     * Writing currentFireBaseStudentService data to Firebase Realtime Database using the REST API
     *
     * @link <a href="https://firebase.google.com/docs/reference/rest/database">Firebase Realtime Database REST API</a>
     */
    @Override
    public FireBaseStudentService updateFireBaseStudentService(FireBaseStudentService fireBaseStudentService) {
        // Build the url path to object to update
        String jsonSingleDataPath = String.format("%s/%s/%s.json",
                firebaseRtdbBaseUrl, FireBaseStudentService.class.getSimpleName(), fireBaseStudentService.getId());

        // Convert the Java object to a JSON string using JSONB
        String requestBodyJson = jsonb.toJson(fireBaseStudentService);

        // Create and Http Request to send an HTTP PUT request to write over existing data
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(jsonSingleDataPath))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBodyJson, StandardCharsets.UTF_8))
                .build();
        try {
            // Send the Http Request
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // Check if the Http Response was successful
            if (httpResponse.statusCode() != 200) {
                String errorMessage = String.format("Update was not successful with status code: %s", httpResponse.statusCode());
                throw new RuntimeException(errorMessage);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fireBaseStudentService;
    }

    /**
     * Remove data from Firebase Realtime Database using the REST API
     *
     * @link <a href="https://firebase.google.com/docs/reference/rest/database">Firebase Realtime Database REST API</a>
     */
    @Override
    public void deleteFireBaseStudentServiceById(String id) {
        // Build the URL path of the Json object to delete
        String jsonSingleDataPath = String.format("%s/%s/%s.json",
                firebaseRtdbBaseUrl, FireBaseStudentService.class.getSimpleName(), id);
        // Create an DELETE Http Request
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(jsonSingleDataPath))
                .DELETE()
                .build();
        try {
            // Send the DELETE Http Request
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // Check if the Http Response was successful
            if (httpResponse.statusCode() != 200) {
                String errorMessage = String.format("Delete was not successful with status code: %s", httpResponse.statusCode());
                throw new RuntimeException(errorMessage);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
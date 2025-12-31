package com.carmgmt.cli;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * CLI client for Car Management API
 */
public class CliApplication {
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];

        try {
            switch (command) {
                case "create-car":
                    handleCreateCar(args);
                    break;
                case "add-fuel":
                    handleAddFuel(args);
                    break;
                case "fuel-stats":
                    handleFuelStats(args);
                    break;
                default:
                    System.err.println("Unknown command: " + command);
                    printUsage();
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Handles create-car command.
     * create-car --brand <brand> --model <model> --year <year>
     */
    private static void handleCreateCar(String[] args) throws Exception {
        String brand = null;
        String model = null;
        Integer year = null;

        // Parse command line arguments
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--brand":
                    if (i + 1 < args.length) {
                        brand = args[++i];
                    }
                    break;
                case "--model":
                    if (i + 1 < args.length) {
                        model = args[++i];
                    }
                    break;
                case "--year":
                    if (i + 1 < args.length) {
                        year = Integer.parseInt(args[++i]);
                    }
                    break;
            }
        }

        if (brand == null || model == null || year == null) {
            throw new IllegalArgumentException(
                    "Missing required parameters. Usage: create-car --brand <brand> --model <model> --year <year>"
            );
        }

        // Build JSON request body
        String jsonBody = String.format(
                "{\"brand\":\"%s\",\"model\":\"%s\",\"year\":%d}",
                escapeJson(brand), escapeJson(model), year
        );

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send request and get response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            System.out.println("Car created successfully!");
            System.out.println(response.body());
        } else {
            System.err.println("Failed to create car. Status: " + response.statusCode());
            System.err.println(response.body());
            System.exit(1);
        }
    }

    /**
     * Handles add-fuel command.
     * add-fuel --carId <id> --liters <liters> --price <price> --odometer <odometer>
     */
    private static void handleAddFuel(String[] args) throws Exception {
        Long carId = null;
        Double liters = null;
        Double price = null;
        Integer odometer = null;

        // Parse command line arguments
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--carId":
                    if (i + 1 < args.length) {
                        carId = Long.parseLong(args[++i]);
                    }
                    break;
                case "--liters":
                    if (i + 1 < args.length) {
                        liters = Double.parseDouble(args[++i]);
                    }
                    break;
                case "--price":
                    if (i + 1 < args.length) {
                        price = Double.parseDouble(args[++i]);
                    }
                    break;
                case "--odometer":
                    if (i + 1 < args.length) {
                        odometer = Integer.parseInt(args[++i]);
                    }
                    break;
            }
        }

        if (carId == null || liters == null || price == null || odometer == null) {
            throw new IllegalArgumentException(
                    "Missing required parameters. Usage: add-fuel --carId <id> --liters <liters> --price <price> --odometer <odometer>"
            );
        }

        // Build JSON request body
        String jsonBody = String.format(
                "{\"liters\":%.2f,\"price\":%.2f,\"odometer\":%d}",
                liters, price, odometer
        );

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars/" + carId + "/fuel"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send request and get response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            System.out.println("Fuel entry added successfully!");
            System.out.println(response.body());
        } else if (response.statusCode() == 404) {
            System.err.println("Car with ID " + carId + " not found");
            System.exit(1);
        } else {
            System.err.println("Failed to add fuel entry. Status: " + response.statusCode());
            System.err.println(response.body());
            System.exit(1);
        }
    }

    /**
     * Handles fuel-stats command.
     * fuel-stats --carId <id>
     */
    private static void handleFuelStats(String[] args) throws Exception {
        Long carId = null;

        // Parse command line arguments
        for (int i = 1; i < args.length; i++) {
            if ("--carId".equals(args[i]) && i + 1 < args.length) {
                carId = Long.parseLong(args[++i]);
            }
        }

        if (carId == null) {
            throw new IllegalArgumentException(
                    "Missing required parameter. Usage: fuel-stats --carId <id>"
            );
        }

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars/" + carId + "/fuel/stats"))
                .GET()
                .build();

        // Send request and get response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Parse JSON response (simplified - in production would use a JSON library)
            String body = response.body();
            displayFuelStats(body);
        } else if (response.statusCode() == 404) {
            System.err.println("Car with ID " + carId + " not found");
            System.exit(1);
        } else {
            System.err.println("Failed to get fuel stats. Status: " + response.statusCode());
            System.err.println(response.body());
            System.exit(1);
        }
    }

    /**
     * Parses and displays fuel statistics in a user-friendly format.
     * Expected JSON format: {"success":true,"data":{"totalFuel":120.0,"totalCost":155.0,"averageConsumption":6.4}}
     */
    private static void displayFuelStats(String jsonResponse) {
        // Simple JSON parsing (for production, use a proper JSON library like Jackson or Gson)
        try {
            // Extract the data object from the ApiResponse format
            // Look for "data":{...} pattern
            int dataStart = jsonResponse.indexOf("\"data\":{");
            if (dataStart == -1) {
                // Fallback to old format parsing
                String cleaned = jsonResponse.replaceAll("[{}\"]", "");
                String[] parts = cleaned.split(",");
                
                Double totalFuel = null;
                Double totalCost = null;
                Double averageConsumption = null;

                for (String part : parts) {
                    String[] keyValue = part.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        
                        switch (key) {
                            case "totalFuel":
                                totalFuel = Double.parseDouble(value);
                                break;
                            case "totalCost":
                                totalCost = Double.parseDouble(value);
                                break;
                            case "averageConsumption":
                                averageConsumption = Double.parseDouble(value);
                                break;
                        }
                    }
                }

                if (totalFuel != null && totalCost != null && averageConsumption != null) {
                    System.out.println("Total fuel: " + String.format("%.0f", totalFuel) + " L");
                    System.out.println("Total cost: " + String.format("%.2f", totalCost));
                    System.out.println("Average consumption: " + String.format("%.1f", averageConsumption) + " L/100km");
                } else {
                    System.out.println(jsonResponse);
                }
                return;
            }
            
            // Extract data object content
            int dataContentStart = dataStart + 7; // Skip "data":{
            int braceCount = 0;
            int dataEnd = dataContentStart;
            boolean inString = false;
            
            for (int i = dataContentStart; i < jsonResponse.length(); i++) {
                char c = jsonResponse.charAt(i);
                if (c == '"' && (i == 0 || jsonResponse.charAt(i-1) != '\\')) {
                    inString = !inString;
                } else if (!inString) {
                    if (c == '{') braceCount++;
                    if (c == '}') {
                        if (braceCount == 0) {
                            dataEnd = i;
                            break;
                        }
                        braceCount--;
                    }
                }
            }
            
            String dataContent = jsonResponse.substring(dataContentStart, dataEnd + 1);
            
            // Parse the data object
            String cleaned = dataContent.replaceAll("[{}\"]", "");
            String[] parts = cleaned.split(",");
            
            Double totalFuel = null;
            Double totalCost = null;
            Double averageConsumption = null;

            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    
                    switch (key) {
                        case "totalFuel":
                            totalFuel = Double.parseDouble(value);
                            break;
                        case "totalCost":
                            totalCost = Double.parseDouble(value);
                            break;
                        case "averageConsumption":
                            averageConsumption = Double.parseDouble(value);
                            break;
                    }
                }
            }

            if (totalFuel != null && totalCost != null && averageConsumption != null) {
                System.out.println("Total fuel: " + String.format("%.0f", totalFuel) + " L");
                System.out.println("Total cost: " + String.format("%.2f", totalCost));
                System.out.println("Average consumption: " + String.format("%.1f", averageConsumption) + " L/100km");
            } else {
                System.out.println(jsonResponse);
            }
        } catch (Exception e) {
            // If parsing fails, just print the raw JSON
            System.out.println(jsonResponse);
        }
    }

    /**
     * Escapes special characters in JSON strings.
     */
    private static String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    /**
     * Prints usage information.
     */
    private static void printUsage() {
        System.out.println("Car Management CLI Client");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  create-car --brand <brand> --model <model> --year <year>");
        System.out.println("  add-fuel --carId <id> --liters <liters> --price <price> --odometer <odometer>");
        System.out.println("  fuel-stats --carId <id>");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  create-car --brand Toyota --model Corolla --year 2018");
        System.out.println("  add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000");
        System.out.println("  fuel-stats --carId 1");
    }
}


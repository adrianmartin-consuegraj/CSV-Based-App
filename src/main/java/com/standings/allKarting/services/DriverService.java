package com.standings.allKarting.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.standings.allKarting.models.Driver;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DriverService {

    String fileName = "C:/Users/Adrian/Desktop/All-Karting-Project/allKarting/files/fileCreated.txt";
    String filePath = "C:/Users/Adrian/Desktop/All-Karting-Project/allKarting/files/json.txt";
    String driversJSON = "C:/Users/Adrian/Desktop/All-Karting-Project/allKarting/files/drivers.txt";
    String csvFilePath = "C:/Users/Adrian/Desktop/All-Karting-Project/allKarting/files/export.csv";

    // the good ones
    String kartingStandings = "C:/Users/Adrian/Desktop/All-Karting-Project/allKarting/files/kartingStandings.csv";
    String currentRace = "C:/Users/Adrian/Desktop/All-Karting-Project/allKarting/files/currentRace.csv";
    String positionPoints = "C:/Users/Adrian/Desktop/All-Karting-Project/allKarting/files/positionPoints.csv";

    //! don't use
    public void createFile() {

        try {
            // Create a File object with the specified file name
            File file = new File(fileName);

            // Check if the file already exists
            if (file.exists()) {
                System.out.println("File already exists.");
            } else {
                // Create a FileWriter to write to the file
                FileWriter writer = new FileWriter(file);

                // You can write data to the file here if needed
                // For example:
                writer.write("Hello, YEE!");

                // Close the FileWriter when done
                writer.close();

                System.out.println("File created: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    //! don't use
    public String returnEndpoint() {
        try {
            // Read the content of the .txt file
            String fileContent = Files.readString(Path.of(filePath));

            // Assuming the .txt file contains JSON data, parse it into a Map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonData = objectMapper.readValue(fileContent, Map.class);

            // Convert the Map back to a JSON string
            return objectMapper.writeValueAsString(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
            return "{\"error\": \"Failed to read or process the file\"}";
        }
    }

    //! don't use
    public void convertJsonToCsv() {
        try {
            // Initialize ObjectMapper for JSON and CSVWriter for CSV
            ObjectMapper objectMapper = new ObjectMapper();
            CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath));

            // Read JSON data from the input file and map it to a list of Driver objects
            List<Driver> drivers = objectMapper.readValue(new File(driversJSON), new TypeReference<List<Driver>>() {});

            // Write CSV data from the list of Driver objects
            for (Driver d : drivers) {
                // Create a CSV record based on the driver's information
                String[] csvData = new String[]{
                        d.getName(),
                        String.valueOf(d.getPoints())
                };

                csvWriter.writeNext(csvData);
            }

            // Close the CSVWriter
            csvWriter.close();
        } catch (IOException e) {
            // Handle the exception (e.g., log the error, return an error message, etc.)
            e.printStackTrace(); // Example: Print the stack trace
        }
    }



    //? (1) working heavenly
    public String updateStandings() {

        String[] nextRecord;
        boolean isFirstRow = true; // Flag 'true' to skip the first row

        try {

            //* Read data from "karting-standings" CSV and store it in a HashMap
            Map<Integer, Driver> standingsData = new HashMap<>();
            CSVReader csvReader1 = new CSVReader(new FileReader(kartingStandings));

            while ((nextRecord = csvReader1.readNext()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip the first row
                }

                int position = Integer.parseInt(nextRecord[0]);
                String name = nextRecord[1];
                int points = Integer.parseInt(nextRecord[2]);
                standingsData.put(position, new Driver(name, points));
            }
            isFirstRow = true;
            csvReader1.close();

            //* Read data from "current-race" CSV and store it in a HashMap
            Map<Integer, String> currentRaceData = new HashMap<>();
            CSVReader csvReader2 = new CSVReader(new FileReader(currentRace));

            while ((nextRecord = csvReader2.readNext()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip the first row
                }

                int position = Integer.parseInt(nextRecord[0]);
                String name = nextRecord[1];
                currentRaceData.put(position, name);
            }
            isFirstRow = true;
            csvReader2.close();

            //* Read data from "position-points" CSV and store it in a HashMap
            Map<Integer, Integer> positionPointsData = new HashMap<>();
            CSVReader csvReader3 = new CSVReader(new FileReader(positionPoints));

            while ((nextRecord = csvReader3.readNext()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip the first row
                }

                int position = Integer.parseInt(nextRecord[0]);
                int points = Integer.parseInt(nextRecord[1]);
                positionPointsData.put(position, points);
            }
            isFirstRow = true;
            csvReader3.close();

            List<Driver> updatedStandingsData = new ArrayList<>();

            for (int ks : standingsData.keySet()) {

                Driver driver = standingsData.get(ks);
                System.out.println("-- POINTS BEFORE: " + driver.getPoints());

                for (Map.Entry<Integer, String> cr : currentRaceData.entrySet()) {
                    if(driver.getName().equalsIgnoreCase(cr.getValue())){

                        int pos = cr.getKey();

                        for (Map.Entry<Integer, Integer> pp : positionPointsData.entrySet()) {

                            if(pos == pp.getKey()){

                                driver.setPoints(driver.getPoints() + pp.getValue());
                                System.out.println("== POINTS UPDATED: " + driver.getPoints());

                            }

                        }

                    }
                }

                updatedStandingsData.add(driver);
            }

            //* Sort drivers by points (descending order)
            updatedStandingsData.sort(Comparator.comparingInt(Driver::getPoints).reversed());

            // Add the "POSITION" header
            String[] csvHeader = { "Position", "Name", "Points" };

            // Convert the sorted updated standings data back to CSV format
            List<String[]> updatedStandingsCsvData = updatedStandingsData.stream()
                    .map(driver -> new String[] {
                            String.valueOf(updatedStandingsData.indexOf(driver) + 1), // Position
                            driver.getName(),
                            String.valueOf(driver.getPoints())
                    })
                    .collect(Collectors.toList());

            // Write the updated CSV data to the "kartingStandings.csv" file
            CSVWriter csvWriter = new CSVWriter(new FileWriter(kartingStandings));

            // Write the CSV header
            csvWriter.writeNext(csvHeader);

            // Write the data
            csvWriter.writeAll(updatedStandingsCsvData);

            csvWriter.close();

            // Convert the updated standings data to a JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String updatedStandingsJson = objectMapper.writeValueAsString(updatedStandingsData);

            return updatedStandingsJson;

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace(); // Example: Print the stack trace
            return "{}"; // Return an empty JSON object or an error message
        }
    }

    //? PERFECT!
    public List<Driver> getAllDrivers(){

        String[] nextRecord;
        boolean isFirstRow = true; // Flag 'true' to skip the first row

        List<Driver> drivers = new ArrayList<>();

        // Read data from "karting-standings" CSV and store it in a HashMap
        try {
            CSVReader csvReaderAllDrivers = new CSVReader(new FileReader(kartingStandings));

            while ((nextRecord = csvReaderAllDrivers.readNext()) != null) {

                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip the first row
                }

                int position = Integer.parseInt(nextRecord[0]);

                String name = nextRecord[1];
                int points = Integer.parseInt(nextRecord[2]);
                Driver d = new Driver(name, points);
                drivers.add(d);
                int lastPosition = position;

            }

        }catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }


        return drivers;
    }

    //? PERFECT!
    public void createDriver(Driver d){

        String[] csvHeader = { "Position", "Name", "Points" };
        int setPosition = 0;

        try {
            int lastPosition = 0;

            // Check if the CSV file is empty
            if (new File(kartingStandings).length() > 0) {
                // Read the existing data to find the last position
                CSVReader csvReader = new CSVReader(new FileReader(kartingStandings));
                String[] lastRow = null;
                String[] nextRow;

                while ((nextRow = csvReader.readNext()) != null) {
                    lastRow = nextRow;
                }

                csvReader.close();

                if (lastRow != null && lastRow.length > 0) {
                    lastPosition = Integer.parseInt(lastRow[0]);
                }
            }

            // Increment the last position by 1 for the new driver
            int newPosition = lastPosition + 1;
            setPosition = newPosition;

            // Create a FileWriter in append mode
            FileWriter fileWriter = new FileWriter(kartingStandings, true);

            // Create a CSVWriter
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            // Create an array to hold the data you want to append
            String[] data = { Integer.toString(newPosition), d.getName(), Integer.toString(d.getPoints()) };

            // Write the data to the CSV file
            csvWriter.writeNext(data);

            // Close the CSVWriter
            csvWriter.close();
        }catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

    }

}

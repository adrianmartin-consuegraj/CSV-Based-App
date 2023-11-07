package com.standings.allKarting.controllers;

import com.standings.allKarting.models.Driver;
import com.standings.allKarting.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DriverController {

    @Autowired
    private DriverService driverService;

    //! don't use
    @GetMapping("/bb")
    public String getAllDrivers(){
        System.out.println("Creating file siiiuuuu . . .");
        //driverService.createFile();
        return "tons of them";
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/json")
    @ResponseBody
    public ResponseEntity<Object> getJsonData() {
        try {
            // Replace this with the logic to retrieve JSON data from your service or source
            // For example, you can call a service method that returns the JSON data
            // Services1 should have a method to return JSON data
            Object jsonData = driverService.returnEndpoint();

            // Check if the JSON data is not null and return it in the response
            if (jsonData != null) {
                return new ResponseEntity<>(jsonData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No data found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/csv")
    public void createCSV(){
        driverService.convertJsonToCsv();
    }


    //? (1) working heavenly
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/updateStandings")
    @ResponseBody
    public String updateStandings(){
        return driverService.updateStandings();
    }

    //? (2) working heavenly
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/drivers")
    List<Driver> allDrivers(){
        return driverService.getAllDrivers();
    }

    //? (3) working heavenly
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/drivers/add")
    String addDriver(@RequestBody Driver d){
        driverService.createDriver(d);
        return "done!";
    }

    //! NOT WORKING -- CHECKEAR PARA QUE PODRÍA SERVIR!
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/drivers/STUFFUUU")
    String addDriver(@PathVariable int id, @RequestParam String params){
        return "leyendo :" + params;
    }


}

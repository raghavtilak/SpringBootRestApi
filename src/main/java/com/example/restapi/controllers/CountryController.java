package com.example.restapi.controllers;

import com.example.restapi.beans.Country;
import com.example.restapi.services.CountryService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class CountryController {

    @Autowired
    CountryService countryService;

    @GetMapping("/getCountries")
    public List<Country> getAllCountries(){
        return countryService.getAllCountries();
    }

    @GetMapping("/getCountries/{id}")
    public Country getCountryById(@PathVariable (value = "id") int id){
        return countryService.getCountryById(id);
    }

    /*
* was getting this error on the server side,
* com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 2 path $
java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 2 path $
* This is becoz i was sending JSON, but retrofit was wrapping it into string, so
* instead of having my first letter of JSON as '{' or '[', the server was receinvg
* the JSON with first letter as '"'. so on the server side the gson was not able to
* deserialize it.
*
* we either remove the '"' at the beginning or at end, or we can either send from here
* only a valid JSON not wrapped in as a String object
*
* we are doing 1st option
* */
    @PostMapping(value="/addCountry")
    public ResponseEntity<String> addCountry(@RequestParam List<MultipartFile> images, @RequestParam("country") String json){


        System.out.println("entered-\n"+json);
        try {
            System.out.println("entered try");

            Gson gson=new Gson();
            Country c=gson.fromJson(json,Country.class);
//            ObjectMapper mapper=new ObjectMapper();
//            c=mapper.readValue(json,Country.class);
            String response=countryService.addCountry(images,c);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("entered catch"+e+"\n"+e.getMessage());

            return new ResponseEntity<>("Some internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping(value="/addCountry")
//    public ResponseEntity<String> addCountry(@RequestParam List<MultipartFile> images, @RequestParam("country") Country c){
//
//        System.out.println("entered-\n"+c.getName());
//        try {
//            System.out.println("entered try");
//
////            Country c=new Country();
//            ObjectMapper mapper=new ObjectMapper();
////            c=mapper.readValue(json,Country.class);
//            String response=countryService.addCountry(images,c);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (Exception e) {
//            System.out.println("entered catch"+e+"\n"+e.getMessage());
//
//            return new ResponseEntity<>("Some internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/updateCountry")
    public String updateCountry(@RequestBody Country c){
        return countryService.updateCountry(c);
    }

    @DeleteMapping("/deleteCountry/{id}")
    public String deleteCountry(@PathVariable (value = "id") int id){
        return countryService.deleteCountry(id);
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = countryService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {

        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    //functions for testing
//    @PostMapping("/addImage")
//    public String addImage(@RequestParam("image") MultipartFile file,@RequestParam("name") String name){
//        return countryService.addImage(file,name);
//    }
//
//    @PostMapping(value="/addImages")
//    public String addImages(@RequestParam List<MultipartFile> images){
//        return countryService.addImages(images);
//    }

}

package com.example.restapi.services;


import com.example.restapi.FileStorageProperties;
import com.example.restapi.beans.Country;
import com.example.restapi.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
@Component
@Service
public class CountryService {

    @Autowired
    CountryRepository repo;
    private final Path fileStorageLocation;

    @Autowired
    public CountryService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Country> getAllCountries(){
        return repo.findAll();
    }


    public Country getCountryById(int id){
        return repo.findById(id).get();
    }

    public String addCountry(List<MultipartFile> images,Country c) throws Exception {

        System.out.println("entered fun");

        c.setId(getKey());

        String paths=getAllFilePaths(images,c.getId());
        c.setImages(paths);

        repo.save(c);
        return "Country Added";
    }

    public String updateCountry(Country c){

        repo.save(c);
        return "Country updated";
    }

    public String deleteCountry(int id){

        repo.deleteById(id);
        return "Country deleted";
    }

    private int getKey(){

        Optional<Country> c=repo.findAll().stream().max(Comparator.comparingInt(Country::getId));
        return c.map(country -> country.getId() + 1).orElse(1);

    }

    private String getAllFilePaths(List<MultipartFile> images, int id) throws Exception{


        StringBuilder s=new StringBuilder();

        for(MultipartFile image:images){
            try {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                // Check if the file's name contains invalid characters
                if(fileName.contains("..")) {

                }

                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/downloadFile/")
                        .path(fileName)
                        .toUriString();
                s.append(fileDownloadUri).append(',');
            } catch (Exception ex) {}
        }
        return s.substring(0,s.length()-1);


    }
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {

            }
        } catch (MalformedURLException ex) {

        }
        return null;
    }

    public String addImage(MultipartFile file,String name) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {

            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();

            System.out.println(fileDownloadUri+ name);
            return fileDownloadUri;

        } catch (Exception ex) {
            return "Some error occured";
        }
    }

//    public String addImages(List<MultipartFile> images) {
//        for(MultipartFile image:images){
//            addImage(image,"");
//        }
//        return "All images dalgyi";
//    }


//    private String getAllFilePaths(MultipartFile[] images, int id) throws Exception{
//
//        StringBuilder s=new StringBuilder();
//        File f=new File("images"+File.separator+ id);
//        if(!f.exists()) f.mkdirs();
//
//        for(MultipartFile image:images){
//            String path=f.getAbsolutePath()+File.separator+image.getOriginalFilename();
//            Files.copy(image.getInputStream(), Paths.get(path));
//            s.append(path).append(',');
//        }
//        return s.substring(0,s.length()-1);
//
//    }


}

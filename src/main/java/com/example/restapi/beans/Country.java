package com.example.restapi.beans;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name = "Country")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class Country {

    @Id
    @Column(name = "id")
    int id;
    @Column(name="name")
    String name;
    @Column(name = "capital")
    String capital;

    @Column(name = "images")
    String images;

    public Country() {
    }

    public Country(int id, String name, String capital, String images) {
        this.id = id;
        this.name = name;
        this.capital = capital;
        this.images = images;
    }

    public Country(String capital, int id,String images, String name) {
        this.id = id;
        this.name = name;
        this.capital = capital;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}

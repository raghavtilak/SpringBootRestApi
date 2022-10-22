package com.example.restapi.repositories;

import com.example.restapi.beans.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//<entity,primary key type>
public interface CountryRepository extends JpaRepository<Country,Integer> {

}

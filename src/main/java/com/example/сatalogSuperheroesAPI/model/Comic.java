package com.example.сatalogSuperheroesAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "comics")
public class Comic {

    @Id
    private BigInteger id;

    @DBRef(lazy = true)
    @JsonIgnore
    private List<Character> characters = new ArrayList<>();

    private String name = "";
    private String description = "";

    private String imageDataBase64;
    private String imageFormat = "";

}

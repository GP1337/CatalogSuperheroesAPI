package com.example.сatalogSuperheroesAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty(value = "")
    private String name;

    @JsonProperty(value = "")
    private String description;

    private String imageDataBase64;

    @JsonProperty(value = "")
    private String imageFormat;

}

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
@Document(collection = "characters")
public class Character {

    @Id
    private BigInteger id;

    @JsonProperty(value = "")
    private String name;

    @JsonProperty(value = "")
    private String description;

    private String imageDataBase64;

    @JsonProperty(value = "")
    private String imageFormat;

    @DBRef(lazy = true)
    @JsonIgnore
    private List<Comic> comics = new ArrayList<>();

}

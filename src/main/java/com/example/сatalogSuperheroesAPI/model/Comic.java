package com.example.сatalogSuperheroesAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(notes = "The database generated product ID")
    private BigInteger id;

    @DBRef(lazy = true)
    @JsonIgnore
    private List<Character> characters = new ArrayList<>();

    @JsonProperty(value = "")
    @ApiModelProperty(notes = "Name of the comic")
    private String name;

    @JsonProperty(value = "")
    @ApiModelProperty(notes = "Description of the comic")
    private String description;

    @ApiModelProperty(notes = "Image of the comic in base64")
    private String imageDataBase64;

    @JsonProperty(value = "")
    @ApiModelProperty(notes = "Image format (jpg, png ...)")
    private String imageFormat;

}

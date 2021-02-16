package com.example.сatalogSuperheroesAPI.repository;

import com.example.сatalogSuperheroesAPI.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CharacterRepository extends MongoRepository<Character, BigInteger> {
}

package com.example.сatalogSuperheroesAPI.service;

import com.example.сatalogSuperheroesAPI.model.Character;
import com.example.сatalogSuperheroesAPI.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CharacterService {

    public static final ArrayList<String> filterFields = new ArrayList<String>(Arrays.asList("name", "description"));

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private CharacterRepository characterRepository;

    public List<Character> getCharacterList(Map<String, String> params){

        if (params == null) {
            return characterRepository.findAll();
        }

        Query query = new Query();

        // handle filter
        for (String filterField : filterFields) {

            String filterParam = params.get(filterField);
            if (filterParam != null) {
                query.addCriteria(Criteria.where(filterField).regex(filterParam));
            }
        }

        // handle ordering
        String orderBy = params.get("orderby");
        if (orderBy != null){

            String[] orderParams = orderBy.split(",");
            String[] orderComponents;

            Sort.Direction direction;

            for (String orderParam : orderParams) {

                orderParam = orderParam.strip();

                orderComponents = orderParam.split(" ");

                direction = Sort.Direction.ASC;

                if (orderComponents.length > 1 && orderComponents[1].toLowerCase().equals("desc")){
                    direction = Sort.Direction.DESC;
                }

                query.with(Sort.by(direction, orderComponents[0]));

            }
        }

        // handle pagination
        String offset = params.get("offset");
        if (offset != null){

            Integer intOffset = stringToInteger(offset);

            if (intOffset != null) {
                query.skip(Long.parseLong(offset));
            }

        }

        String limit = params.get("limit");
        if (limit != null){

            Integer intLimit = stringToInteger(offset);

            if (intLimit != null) {
                query.limit(Integer.parseInt(limit));
            }

        }

        return mongoTemplate.find(query, Character.class);

    }

    public Character getCharacterById(BigInteger id){

        return characterRepository.findById(id).orElse(null);

    }

    public Character addCharacter(Character character){

       return characterRepository.save(character);

    }

    public Character changeCharacterById(BigInteger id, Character character){

        character.setId(id);

        return characterRepository.save(character);

    }

    private Integer stringToInteger(String string){

        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }

    }

}

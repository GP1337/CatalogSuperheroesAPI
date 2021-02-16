package com.example.сatalogSuperheroesAPI.service;

import com.example.сatalogSuperheroesAPI.model.Character;
import com.example.сatalogSuperheroesAPI.model.Comic;
import com.example.сatalogSuperheroesAPI.repository.CharacterRepository;
import com.example.сatalogSuperheroesAPI.repository.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.*;

@Service
public class ComicService {

    public static final ArrayList<String> filterFields = new ArrayList<String>(Arrays.asList("name", "description"));

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private ComicRepository comicRepository;

    public List<Comic> getComicsList(Map<String, String> params){

        if (params == null) {
            return comicRepository.findAll();
        }

        Query query = new Query();

        // handle filter
        for (String filterField : filterFields) {

            String filterParam = params.get(filterField);
            if (filterParam != null) {
                query.addCriteria(Criteria.where(filterField).regex(filterParam));
            }
        }

        String characters = params.get("characters");
        if (characters != null){


            List<String> characterNameList = Arrays.asList(characters.split(","));

            characterNameList.forEach(s -> s = s.strip());

            String regEx = StringUtils.collectionToDelimitedString(characterNameList, "|");

            Query characterQuery = new Query();
            characterQuery.addCriteria(Criteria.where("name").regex(regEx));

            List<Character> characterList = mongoTemplate.find(characterQuery, Character.class);

            query.addCriteria(Criteria.where("characters").in(characterList));

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
            query.skip(Long.parseLong(offset));
        }

        String limit = params.get("limit");
        if (limit != null){
            query.limit(Integer.parseInt(limit));
        }



        return mongoTemplate.find(query, Comic.class);

    }

    public Comic getComicById(BigInteger id){

        return comicRepository.findById(id).orElse(null);

    }

    public Comic addComic(Comic comic){

        return comicRepository.save(comic);

    }


    public Comic changeCharacterById(BigInteger id, Comic comic){

        Comic dbComic = getComicById(id);

        try {

            for (Field field : comic.getClass().getDeclaredFields()) {

                field.setAccessible(true);

                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(DBRef.class) || field.get(comic) == null) {
                    continue;
                }

                field.set(dbComic, field.get(comic));

            }
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return comicRepository.save(dbComic);

    }


}

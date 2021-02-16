package com.example.сatalogSuperheroesAPI.service;

import com.example.сatalogSuperheroesAPI.model.Comic;
import com.example.сatalogSuperheroesAPI.repository.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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

        comic.setId(id);

        return comicRepository.save(comic);

    }


}

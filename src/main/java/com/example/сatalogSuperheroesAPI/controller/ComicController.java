package com.example.сatalogSuperheroesAPI.controller;

import com.example.сatalogSuperheroesAPI.model.Character;
import com.example.сatalogSuperheroesAPI.model.Comic;
import com.example.сatalogSuperheroesAPI.model.ResponseMessage;
import com.example.сatalogSuperheroesAPI.service.CharacterService;
import com.example.сatalogSuperheroesAPI.service.ComicCharacterRelationService;
import com.example.сatalogSuperheroesAPI.service.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comics")
public class ComicController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private ComicService comicService;

    @Autowired
    private ComicCharacterRelationService comicCharacterRelationService;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<Comic>>> getComicList(@RequestParam(required = false) String name, @RequestParam(required = false) String description,
                                                        @RequestParam(required = false) String orderby, @RequestParam(required = false) String offset,
                                                        @RequestParam(required = false) String limit){

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", description);
        params.put("orderby", orderby);
        params.put("offset", offset);
        params.put("limit", limit);

        return new ResponseEntity(new ResponseMessage(comicService.getComicsList(params)), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<Character>> getComicById(@PathVariable BigInteger id){

        Comic dbComic = comicService.getComicById(id);

        if (dbComic == null){
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getElementNotFoundMessage(Comic.class, id)), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(new ResponseMessage(dbComic), HttpStatus.OK);

    }

    @GetMapping("/{id}/characters")
    public ResponseEntity<ResponseMessage<List<Character>>> getComicsCharacters(@PathVariable BigInteger id){

        Comic dbComic = comicService.getComicById(id);

        if (dbComic == null){
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getElementNotFoundMessage(Comic.class, id)), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(new ResponseMessage(dbComic.getCharacters()), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<ResponseMessage<Comic>> postComic(@RequestBody Comic comic){

        if (comic.getId() != null) {
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getIdUnacceptableMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(comicService.addComic(comic), HttpStatus.OK);

    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseMessage<Comic>> postComicById(@RequestBody Comic comic, @PathVariable(required = false /*post can be used either to update or to create character*/) BigInteger id){

        if (comic.getId() != null) {
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getIdUnacceptableMessage()), HttpStatus.BAD_REQUEST);
        }

        if (comicService.getComicById(id) == null) {
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getElementNotFoundMessage(comic.getClass(), id)), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(comicService.changeCharacterById(id, comic), HttpStatus.OK);

    }

    @PutMapping
    public ResponseEntity<ResponseMessage<Comic>> putComic(@RequestBody Comic comic, @PathVariable BigInteger id){

        if (comic.getId() != null){
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getIdUnacceptableMessage()), HttpStatus.BAD_REQUEST);
        }

        comicService.changeCharacterById(id, comic);

        return new ResponseEntity(ResponseMessage.ResponsePostPutOk(), HttpStatus.OK);

    }

    @PostMapping("/{comicId}/characters/{characterId}")
    public ResponseEntity<ResponseMessage> addComicCharacter(@PathVariable BigInteger comicId, @PathVariable BigInteger characterId){

        Character character = characterService.getCharacterById(characterId);
        Comic comic = comicService.getComicById(comicId);

        List<String> errors = new ArrayList();

        if (character == null){
            errors.add(ResponseMessage.getElementNotFoundMessage(Character.class, characterId));
        }

        if (comic == null){
            errors.add(ResponseMessage.getElementNotFoundMessage(Comic.class, comicId));
        }

        if (errors.size() > 0){
            return new ResponseEntity(new ResponseMessage(errors.toString()), HttpStatus.NOT_FOUND);
        }

        comicCharacterRelationService.saveRelation(character, comic);

        return new ResponseEntity(ResponseMessage.ResponsePostPutOk(), HttpStatus.OK);

    }

    @DeleteMapping("/{comicId}/characters/{characterId}")
    public ResponseEntity<ResponseMessage> removeCharactersComics(@PathVariable BigInteger comicId, @PathVariable BigInteger characterId){

        Character character = characterService.getCharacterById(characterId);
        Comic comic = comicService.getComicById(comicId);

        List<String> errors = new ArrayList();

        if (character == null){
            errors.add(ResponseMessage.getElementNotFoundMessage(Character.class, characterId));
        }

        if (comic == null){
            errors.add(ResponseMessage.getElementNotFoundMessage(Comic.class, characterId));
        }

        if (errors.size() > 0){
            return new ResponseEntity(new ResponseMessage(errors.toString()), HttpStatus.NOT_FOUND);
        }

        comicCharacterRelationService.saveRelation(character, comic);

        return new ResponseEntity(ResponseMessage.ResponsePostPutOk(), HttpStatus.OK);

    }
}

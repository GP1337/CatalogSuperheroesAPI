package com.example.сatalogSuperheroesAPI.controller;

import com.example.сatalogSuperheroesAPI.model.Character;
import com.example.сatalogSuperheroesAPI.model.Comic;
import com.example.сatalogSuperheroesAPI.model.ResponseMessage;
import com.example.сatalogSuperheroesAPI.service.CharacterService;
import com.example.сatalogSuperheroesAPI.service.ComicCharacterRelationService;
import com.example.сatalogSuperheroesAPI.service.ComicService;
import io.swagger.annotations.*;
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
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private ComicService comicService;

    @Autowired
    private ComicCharacterRelationService comicCharacterRelationService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<ResponseMessage<List<Character>>> getCharacterList(@RequestParam(required = false) String name, @RequestParam(required = false) String description,
                                                            @RequestParam(required = false) String orderby, @RequestParam(required = false) String offset,
                                                            @RequestParam(required = false) String limit){

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", description);
        params.put("orderby", orderby);
        params.put("offset", offset);
        params.put("limit", limit);

        return new ResponseEntity(new ResponseMessage(characterService.getCharacterList(params)), HttpStatus.OK);

    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<ResponseMessage<Character>> getCharacterById(@PathVariable BigInteger id){

        Character dbCharacter = characterService.getCharacterById(id);

        if (dbCharacter == null){
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getElementNotFoundMessage(Character.class, id)), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(new ResponseMessage(dbCharacter), HttpStatus.OK);

    }


    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseMessage<Character>> postCharacter(@RequestBody Character character){

        if (character.getId() != null) {
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getIdUnacceptableMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new ResponseMessage(characterService.addCharacter(character)), HttpStatus.OK);

    }


    @PostMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseMessage<Character>> postCharacterById(@RequestBody Character character, @PathVariable BigInteger id){

        if (character.getId() != null) {
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getIdUnacceptableMessage()), HttpStatus.BAD_REQUEST);
        }

        if (characterService.getCharacterById(id) == null) {
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getElementNotFoundMessage(character.getClass(), id)), HttpStatus.NOT_FOUND);
        }

        characterService.changeCharacterById(id, character);

        return new ResponseEntity(ResponseMessage.ResponsePostPutOk(), HttpStatus.OK);

    }

    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseMessage<Character>> putCharacter(@RequestBody Character character, @PathVariable BigInteger id){

        if (character.getId() != null){
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getIdUnacceptableMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new ResponseMessage(characterService.changeCharacterById(id, character)), HttpStatus.OK);

    }

    @GetMapping(path = "/{id}/comics", produces = "application/json")
    public ResponseEntity<ResponseMessage<List<Comic>>> getCharactersComics(@PathVariable BigInteger id){

        Character dbCharacter = characterService.getCharacterById(id);

        if (dbCharacter == null){
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getElementNotFoundMessage(Character.class, id)), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(new ResponseMessage(dbCharacter.getComics()), HttpStatus.OK);

    }

    @PostMapping(path = "/{characterId}/comics/{comicId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseMessage> addCharactersComics(@PathVariable BigInteger characterId, @PathVariable BigInteger comicId){

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

    @DeleteMapping(path = "/{characterId}/comics/{comicId}", produces = "application/json")
    @ApiOperation(value = "RemoveComicFromCharacterList", notes = "Remove comic from character's comics list")
    public ResponseEntity<ResponseMessage> removeCharactersComics(@PathVariable BigInteger characterId, @PathVariable BigInteger comicId){

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
